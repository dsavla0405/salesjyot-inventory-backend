package com.example.inventorygenius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.entity.StockInward;
import com.example.inventorygenius.entity.Storage;
import com.example.inventorygenius.service.ItemSupplierService;
import com.example.inventorygenius.service.StockCountService;
import com.example.inventorygenius.service.StockInwardService;
import com.example.inventorygenius.service.StockService;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/stockInward")
public class StockInwardController {

    @Autowired
    private StockInwardService stockInwardService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ItemSupplierService itemSupplierService;

    @Autowired
    private StockCountService stockCountService;

    @PostMapping
    @Transactional
    public ResponseEntity<StockInward> addStockInward(@RequestBody StockInward stockInward) {
        System.out.println("loc name = " + stockInward.getLocation().getLocationName());

        StockInward savedStockInward = new StockInward();

        savedStockInward.setDate(stockInward.getDate());
        savedStockInward.setItem(stockInward.getItem());
        savedStockInward.setQty(stockInward.getQty());
        savedStockInward.setSkucode(stockInward.getSkucode());
        savedStockInward.setUserEmail(stockInward.getUserEmail());

        Long stockInwardId = stockInward.getStockInwardId();
        String number = String.valueOf(stockInwardId);

        Stock stock = new Stock();
        stock.setDate(LocalDate.now());
        stock.setSkucode(stockInward.getSkucode());
        stock.setAddQty(stockInward.getQty());
        stock.setSubQty("0");
        stock.setItem(stockInward.getItem());
        stock.setSource("stock inward");
        stock.setMessage("stock inward added");
        stock.setNumber("id = " + number);
        stock.setUserEmail(stockInward.getUserEmail());
        stock.setLocation(stockInward.getLocation());
        Stock savedStock = stockService.addStock(stock);
        System.out.println(savedStock.getMessage());
        savedStockInward.setStock(savedStock);

        savedStockInward.setLocation(stockInward.getLocation());

        String skuCode = stockInward.getSkucode();
        StockCount stockCount = stockCountService.getStockCountBySKUCode(skuCode, savedStockInward.getUserEmail());
        if (stockCount == null) {
            stockCount = new StockCount();
            stockCount.setCount(Double.parseDouble(stockInward.getQty()));

            Item retrievedItem = itemSupplierService.getItemBySKUCode(skuCode);
            stockCount.setItem(retrievedItem);
            System.out.println("stock count initialize with skucode : " + stockCount.getItem().getSKUCode());
        } else {
            double currentCount = stockCount.getCount();
            double additionalCount = Double.parseDouble(stockInward.getQty());
            stockCount.setCount(currentCount + additionalCount);
            System.out.println("stock count initialize with skucode : " + stockCount.getItem().getSKUCode());

        }
        stockCount.setUserEmail(stockInward.getUserEmail());

        stockCountService.saveStockCount(stockCount);
        

        StockInward si = stockInwardService.addStockInward(savedStockInward);

        Stock stock2 = si.getStock();
        stock2.setNumber("id = " + String.valueOf(si.getStockInwardId()));
        stockService.updateStock(stock2.getStockId(), stock2);
        return new ResponseEntity<>(savedStockInward, HttpStatus.CREATED);
    }



    @GetMapping
    public ResponseEntity<List<StockInward>> getAllStock() {
        List<StockInward> stocks = stockInwardService.getAllStockInward();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
public void deleteStockInward(@PathVariable("id") Long stockInwardId) {
    System.out.println("Deleting StockInward with id: " + stockInwardId);
    try {
        stockInwardService.deleteStockInwardById(stockInwardId);
        System.out.println("Deleted StockInward with id: " + stockInwardId);
    } catch (Exception e) {
        System.err.println("Error deleting StockInward with id " + stockInwardId + ": " + e.getMessage());
        e.printStackTrace();
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<StockInward> updateStockInward(@PathVariable Long id, @RequestBody StockInward stockInwardDetails) {
        StockInward updatedStockInward = stockInwardService.updateStockInward(id, stockInwardDetails);

        Stock stock = updatedStockInward.getStock();

        StockCount stockCount = stockCountService.getStockCountBySKUCode(stockInwardDetails.getSkucode(), stockInwardDetails.getUserEmail());
        stockCount.setCount(Double.parseDouble(updatedStockInward.getQty()));


        if (stock == null) {
            // Handle the case where stock is null, e.g., by creating a new Stock or returning an error
            // For example, you could throw an exception or log an error
            // throw new RuntimeException("Stock entity is null for StockInward with id " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println(stock.getMessage());

        
        stock.setSkucode(updatedStockInward.getSkucode());
        stock.setAddQty(updatedStockInward.getQty());
        stock.setLocation(updatedStockInward.getLocation());

        // Save the updated Stock entity
        stockService.updateStock(stock.getStockId(), stock);

        return new ResponseEntity<>(updatedStockInward, HttpStatus.OK);
    }

    @GetMapping("/user/email")
    public List<StockInward> getStockInwardsByUser(@RequestParam String email) {
        return stockInwardService.getStockInwardsByUser(email);
    }
    
}
