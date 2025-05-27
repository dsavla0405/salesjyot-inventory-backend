package com.example.inventorygenius.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventorygenius.repository.PickListDataRepository;
import com.example.inventorygenius.entity.Order;
import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.BomItem;
import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.Storage;
import com.example.inventorygenius.entity.PickListData;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.service.OrderService;
import com.example.inventorygenius.service.StorageService;
import com.example.inventorygenius.service.ItemSupplierService;
import com.example.inventorygenius.service.BomService;
import com.example.inventorygenius.service.StockService;
import com.example.inventorygenius.service.StockCountService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PickListDataService {

    @Autowired
    private PickListDataRepository pickListDataRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ItemSupplierService itemSupplierService;

    @Autowired
    private BomService bomService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockCountService stockCountService;

    // Get all picklist data
    public List<PickListData> getAllPickListData(String email) {
        return pickListDataRepository.findByUserEmail(email);
    }

    // Get picklist data by ID
    public PickListData getPickListDataById(Long pickListId) {
        return pickListDataRepository.findById(pickListId).orElse(null);
    }

    // Add new picklist data
    @Transactional
    public PickListData addPickListData(PickListData pickListData) {
        Order order = orderService.findByOrderNo(pickListData.getOrderNo(), pickListData.getUserEmail());
        pickListData.setOrder(order);
        

        System.out.println("sellerSKU = " + pickListData.getSellerSKU());
        System.out.println("description = " + pickListData.getDescription());
        System.out.println("bin number = " + pickListData.getBinNumber());
        System.out.println("rack number = " + pickListData.getRackNumber());
        Item itemP = itemSupplierService.getItemBySKUCode(pickListData.getSkucode());
        System.out.println("sku for stoorage = " + itemP.getSKUCode());
        Storage storage = storageService.getStorageByBinAndRack(pickListData.getBinNumber(), pickListData.getRackNumber(), itemP.getSKUCode());
        
        pickListData.setStorage(storage);
        pickListData.setItem(itemP);

                Stock stock = new Stock();
                stock.setItem(itemP);
                stock.setSkucode(itemP.getSKUCode());
                stock.setDate(LocalDate.now());
                stock.setAddQty("0");
                stock.setSubQty(String.valueOf(pickListData.getPickQty()));
                stock.setSource("picklist/order");
                stock.setLocation(order.getLocation());
                stock.setMessage("pickList generated for order");
                stock.setNumber("pickList Number = " + pickListData.getPickListNumber() + " order no = " + String.valueOf(order.getOrderNo()));

                stockService.addStock(stock);
            

            changeStockCount(itemP, pickListData.getPickQty(), pickListData.getUserEmail());

        

        return pickListDataRepository.save(pickListData);
    }

    public void changeStockCount(Item i, Double qty, String email){
        System.out.println("skucode1 - " + i.getSKUCode());
        StockCount s = stockCountService.getStockCountBySKUCode(i.getSKUCode(), email);
                Double prevCount = s.getCount();
                s.setCount(prevCount - qty);
                stockCountService.updateStockCount(s);
    }


    // Update picklist data
    public PickListData updatePickListData(Long pickListId, PickListData pickListData) {
        pickListData.setPickListId(pickListId);
        return pickListDataRepository.save(pickListData);
    }

    // Delete picklist data
    public void deletePickListData(Long pickListId) {
        pickListDataRepository.deleteById(pickListId);
    }

    @Transactional
    public List<PickListData> deletePickListDataByPickListNumber(Long pickListNumber, String email) {
        // Find the list of PickListData objects by pickListNumber
        List<PickListData> pickListDataList = pickListDataRepository.findByPickListNumberAndUserEmail(pickListNumber, email);
        
        
        if (!pickListDataList.isEmpty()) {
        for(PickListData p : pickListDataList){
            Item itemP = p.getItem();
            
                Order order = orderService.findByOrderNo(p.getOrderNo(), p.getUserEmail());
                order.setOrderStatus("Order Received");
                orderService.updateOrder(order.getOrderId(), order);
                

                Stock stock = new Stock();
                stock.setItem(itemP);
                stock.setSkucode(itemP.getSKUCode());
                stock.setDate(LocalDate.now());
                stock.setAddQty(String.valueOf(p.getPickQty()));
                stock.setSubQty("0");
                stock.setLocation(p.getOrder().getLocation());
                stock.setSource("picklist/order");
                stock.setMessage("pickList deleted");
                stock.setNumber("pickList Number = " + p.getPickListNumber() + " order no = " + String.valueOf(p.getOrder().getOrderNo()));

                stockService.addStock(stock);

                StockCount stockCount = stockCountService.getStockCountBySKUCode(itemP.getSKUCode(), p.getUserEmail());
                Double prevCount = stockCount.getCount();
                stockCount.setCount(prevCount + p.getPickQty());
                stockCountService.updateStockCount(stockCount);

                pickListDataRepository.delete(p);
                
            }
                        
            return pickListDataList;
        } else {
            throw new IllegalArgumentException("No picklist data found with the provided picklist number: " + pickListNumber);
        }
    }

    public List<PickListData> findByPickListNumber(Long pickListNumber, String email) {
        return pickListDataRepository.findByPickListNumberAndUserEmail(pickListNumber, email);
    }
    
}
       
