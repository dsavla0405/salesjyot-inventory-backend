package com.example.inventorygenius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.inventorygenius.Exception.CustomStockException;
import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.Location;
import com.example.inventorygenius.entity.Order;
import com.example.inventorygenius.entity.OrderData;
import com.example.inventorygenius.entity.PickList;
import com.example.inventorygenius.entity.PickListData;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.entity.Storage;
import com.example.inventorygenius.entity.BomItem;

import com.example.inventorygenius.repository.PickListRepository;
import com.example.inventorygenius.service.BomService;
import com.example.inventorygenius.service.ItemSupplierService;
import com.example.inventorygenius.service.OrderService;
import com.example.inventorygenius.service.PickListDataService;
import com.example.inventorygenius.service.PickListService;
import com.example.inventorygenius.service.StockCountService;
import com.example.inventorygenius.service.StockService;
import com.example.inventorygenius.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import com.example.inventorygenius.Exception.CustomStockException;


@RestController
@RequestMapping("/picklists")
public class PickListController {

    @Autowired
    private PickListService pickListService;

    @Autowired
    private StockService stockService;

    @Autowired
    private PickListRepository pickListRepository;

    @Autowired
    private StockCountService stockCountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BomService bomService;

    @Autowired
    private ItemSupplierService itemSupplierService;

    @Autowired 
    private StorageService storageService;

    @Autowired 
    private PickListDataService pickListDataService;

    @PostMapping("/not/generated/orders") // Changed to POST as we are sending data in the request body
    public List<Order> getNotGeneratedOrders(@RequestBody Location location) {
        return pickListService.getAllNotGeneratedOrders(location, location.getUserEmail());
    }


    @GetMapping
    public ResponseEntity<List<PickList>> getAllPickLists() {
        List<PickList> pickLists = pickListService.getAllPickLists();
        return ResponseEntity.ok(pickLists);
    }

    @GetMapping("/user/email")
    public ResponseEntity<List<PickList>> getAllPickLists(@RequestParam String email) {
        List<PickList> pickLists = pickListService.getPickListsByUser(email);
        return ResponseEntity.ok(pickLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PickList> getPickListById(@PathVariable Long id) {
        Optional<PickList> pickList = pickListService.getPickListById(id);
        return pickList.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional()
public ResponseEntity<PickList> createPickList(@RequestBody PickList pickList) {

     PickList createdPickList = pickListService.createPickList(pickList);

     return ResponseEntity.status(HttpStatus.CREATED).body(createdPickList);
}

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletePickList(@PathVariable Long id, @RequestParam String email) {

        Optional<PickList> pickListOptional = pickListRepository.findById(id);

        PickList pickList = pickListOptional.orElseThrow(() -> new IllegalArgumentException("PickList not found with id: " + id));

        pickListService.deletePickList(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orderData")
    public List<OrderData> getOrdersData(@RequestParam String email) {
        return pickListService.getOrderDatas(email);
    }


    @GetMapping("/getData")
    public List<PickListData> getData(@RequestParam String email) {
        return pickListService.getData(email);
    }
    
    public boolean isCurrentDateBetween(Bom bom) {
        LocalDate currentDate = LocalDate.now();
    
        Date startDate = bom.getDefaultStartDate();
        Date endDate = bom.getDefaultEndDate();
    
        LocalDate localStartDate = startDate != null ? convertToLocalDate(startDate) : null;
        LocalDate localEndDate = endDate != null ? convertToLocalDate(endDate) : null;
    
        if (localStartDate != null && localEndDate != null) {
            return (currentDate.isEqual(localStartDate) || currentDate.isAfter(localStartDate)) &&
                   (currentDate.isEqual(localEndDate) || currentDate.isBefore(localEndDate));
        } else if (localStartDate != null) {
            return currentDate.isEqual(localStartDate) || currentDate.isAfter(localStartDate);
        } else if (localEndDate != null) {
            return currentDate.isEqual(localEndDate) || currentDate.isBefore(localEndDate);
        } else {
            return false; // Or handle the case where both dates are null if necessary
        }
    }
    
    private LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
    }
    

    @GetMapping("/getSelectedOrderData")
    public List<OrderData> getMethodName(@RequestParam String orderNo, @RequestParam String bomCode, @RequestParam String email) {
       String bomC = "";
        List<Order> orders = orderService.findByOrderNo(orderNo, email);
       for(Order order : orders){
            for(Item item : order.getItems()){
                for (Bom bom : item.getBoms()){
                    if (item.getBoms().size() > 0 && bomCode.equals("")){
                        System.out.println("in 1");
                        throw new IllegalArgumentException("Select a bomCode");
                    }
                    if(item.getBoms().size() > 0){ 
                        System.out.println("in 2");
                            bomC = bomCode;    
                    }
                    if (item.getBoms().size() == 0) {
                        System.out.println("in 3");
                        bomC = "";
                    }
                    
                }
            }
       }
       System.out.println("bom in picklist merge rows = " + bomC);
       List<OrderData> oo = new ArrayList<>();
       if(bomC.length() > 0){
        Bom bom = bomService.getBomByBomCode(bomC, email);
        System.out.println("calling orderData with bom");
        oo = pickListService.getOrderData(bom);
       }
       else {
        System.out.println("calling orderDatas");
        oo = pickListService.getOrderDatas(email);
       }
        List<OrderData> orderDataList = new ArrayList<>();
        for (OrderData o : oo){
            if (o.getOrderNo().equals(orderNo)){
                orderDataList.add(o);
            }
        }
        return orderDataList;
    }


    @GetMapping("/merged/picklist")
    public List<PickListData> mergedPickListDatas(@RequestParam String email) {
        List<PickListData> allPickListDatas = pickListDataService.getAllPickListData(email);
        List<PickListData> mergedPickListData = new ArrayList<>();
    
        // Map to store PickListData grouped by picklistNumber
        Map<Long, List<PickListData>> groupedByPicklistNumber = new HashMap<>();
    
        // Group PickListData by picklistNumber
        for (PickListData p : allPickListDatas) {
            groupedByPicklistNumber
                .computeIfAbsent(p.getPickListNumber(), k -> new ArrayList<>())
                .add(p);
        }
    
        for (List<PickListData> group : groupedByPicklistNumber.values()) {
            Map<String, PickListData> aggregatedDataBySellerSKU = new HashMap<>();
    
            for (PickListData p : group) {
                String sellerSKU = p.getItem().getSellerSKUCode();
                double qty = p.getQty();
                double pickQty = p.getPickQty();
                LocalDate date = p.getDate();
                String description = p.getItem().getDescription();
                String bin = p.getStorage().getBinNumber();
                String rack = p.getStorage().getRackNumber();
                Long pickListId = p.getPickListId();
    
                if (aggregatedDataBySellerSKU.containsKey(sellerSKU)) {
                    PickListData existingData = aggregatedDataBySellerSKU.get(sellerSKU);
                    existingData.setQty(existingData.getQty() + qty);
                    existingData.setPickQty(existingData.getPickQty() + pickQty);
                } else {
                    PickListData newData = new PickListData();
                    newData.setPickListNumber(p.getPickListNumber());
                    newData.setSellerSKU(sellerSKU);
                    newData.setDate(date);
                    newData.setDescription(description);
                    newData.setQty(qty);
                    newData.setBinNumber(bin);
                    newData.setRackNumber(rack);
                    newData.setPickQty(pickQty);
                    newData.setPickListId(pickListId);
                    aggregatedDataBySellerSKU.put(sellerSKU, newData);
                }
            }
    
            mergedPickListData.addAll(aggregatedDataBySellerSKU.values());
        }
    
        return mergedPickListData;
    }   
    
    @GetMapping("/boms/{orderNo}")
    public List<Bom> getBomWithOrderNo(@PathVariable String orderNo, @RequestParam String email){
        return pickListService.getOrdersWithBom(orderNo, email);
    }

    @GetMapping("/bom/default/bomCode")
    public String getDefaultBom(@RequestParam String orderNo, @RequestParam String email) {
        return pickListService.getDefaultBomCode(orderNo, email);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateScannedItem(
            @RequestParam Long picklistNumber, 
            @RequestParam String sku, 
            @RequestParam Double scannedQty,
            @RequestParam String email) {
        // Call the validation service and return the result wrapped in ResponseEntity
        boolean isValid = pickListService.isScannedItemValid(picklistNumber, sku, scannedQty, email);
        return ResponseEntity.ok(isValid);
    }

}

