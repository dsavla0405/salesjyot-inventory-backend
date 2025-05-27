package com.example.inventorygenius.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.BomItem;
import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.Return;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.repository.ReturnRepository;

@Service
public class ReturnService {

    @Autowired
    private ReturnRepository returnRepository;

    @Autowired
    private ItemSupplierService itemSupplierService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockCountService stockCountService;

    // Method to add a new item
    public Return addReturn(Return r) {
        if(r.getOkStock().equals("Yes")){
        System.out.println("Order is returned. Updating stock.");
            Stock stock = new Stock();
            Item item = itemSupplierService.getItemBySKUCode(r.getItem().getSKUCode());

            if(item == null) {
                System.out.println("Item is null for SKUCode: " + r.getItem().getSKUCode());
            } else {
                System.out.println("Item found: " + item);
            }

            if (item != null && item.getBoms().size() > 0) {
                stock.setDate(LocalDate.now());
                stock.setSkucode(item.getSKUCode());
                stock.setSubQty("0");

                for (Bom bom : item.getBoms()) {
                    for (BomItem bomItem : bom.getItemsInBom()) {
                        if (bomItem.getItem().getSKUCode().equals(item.getSKUCode())) {
                            stock.setAddQty(String.valueOf(Double.parseDouble(bomItem.getQty())));
                        } else {
                            Stock s = new Stock();
                            stock.setDate(LocalDate.now());
                            s.setSubQty("0");
                            s.setItem(item);
                            s.setAddQty(String.valueOf(Double.parseDouble(bomItem.getQty())));
                            s.setSkucode(bomItem.getBomItem());
                            s.setSource("Return");
                            s.setMessage("Item Returned and Stock is good");
                            s.setNumber("Return Code = " + r.getReturnCode());
                            s.setLocation(r.getLocation());
                            System.out.println("Adding stock for BOM item: " + s);
                            stockService.addStock(s);
                        }
                    }
                }

                stock.setItem(r.getItem());
                stock.setSource("Return");
                stock.setMessage("Item Returned and Stock is good");
                stock.setNumber("Return Code = " + r.getReturnCode());
                stock.setLocation(r.getLocation());
            } else {
                stock.setDate(LocalDate.now());
                stock.setSkucode(r.getItem().getSKUCode());
                stock.setSubQty("0");
                stock.setAddQty(String.valueOf(r.getOrder().getQty()));
                stock.setItem(r.getItem());
                stock.setSource("Return");
                stock.setLocation(r.getLocation());
                stock.setMessage("Item returned and Stock is good");
                stock.setNumber("Return code = " + r.getReturnCode());
            }

            System.out.println("Adding stock: " + stock);
            stockService.addStock(stock);

            StockCount sc = new StockCount();
            if (r.getItem().getBoms().size() > 0) {
                for (Bom b : r.getItem().getBoms()) {
                    for (BomItem bomItem : b.getItemsInBom()) {
                        if (bomItem.getItem().getSKUCode().equals(r.getItem().getSKUCode())) {
                            sc = stockCountService.getStockCountBySKUCode(r.getItem().getSKUCode(), r.getUserEmail());
                            sc.setCount(sc.getCount() + r.getOrder().getQty() * Double.parseDouble(bomItem.getQty()));
                        } else {
                            StockCount scBom = stockCountService.getStockCountBySKUCode(bomItem.getItem().getSKUCode(), r.getUserEmail());
                            scBom.setCount(scBom.getCount() + r.getOrder().getQty() * Double.parseDouble(bomItem.getQty()));
                            System.out.println("Updating stock count for BOM item: " + scBom);
                            stockCountService.updateStockCount(scBom);
                        }
                    }
                }
            } else {
                sc = stockCountService.getStockCountBySKUCode(r.getItem().getSKUCode(), r.getUserEmail());
                sc.setCount(sc.getCount() + r.getOrder().getQty());
            }
            System.out.println("Updating stock count1: " + sc);
            stockCountService.updateStockCount(sc);
        }
        
        return returnRepository.save(r);
    }

    // Method to get all items
    public List<Return> getAllReturns() {
        return returnRepository.findAll();
    }

    public void deleteReturnById(Long id) {
        returnRepository.deleteById(id);
    }

    public Return updateStock(Long id, Return stockDetails) {
        Return stock = returnRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BOM not found with id: " + id));

                stock.setDate(stockDetails.getDate());
                stock.setSkucode(stockDetails.getSkucode());
                stock.setPortal(stockDetails.getPortal());
                stock.setOrderNo(stockDetails.getOrderNo());
                stock.setReturnCode(stockDetails.getReturnCode());
                stock.setTrackingNumber(stockDetails.getTrackingNumber());
                stock.setOkStock(stockDetails.getOkStock());
                stock.setSentForTicketOn(stockDetails.getSentForTicketOn());
                stock.setSentForRaisingTicketOn(stockDetails.getSentForRaisingTicketOn());

        return returnRepository.save(stock);
    }

    public List<Return> getReturnsByUser(String email){
        return returnRepository.findByUserEmail(email);
    }

}
