package com.example.inventorygenius.controller;

import com.example.inventorygenius.entity.StockTransfer;
import com.example.inventorygenius.service.StockTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocktransfer")
public class StockTransferController {

    @Autowired
    private StockTransferService stockTransferService;

    // Get all stock transfers
    @GetMapping("/all")
    public ResponseEntity<List<StockTransfer>> getAllStockTransfers() {
        List<StockTransfer> stockTransfers = stockTransferService.getAllStockTransfers();
        return new ResponseEntity<>(stockTransfers, HttpStatus.OK);
    }

    // Get a single stock transfer by ID
    @GetMapping("/{id}")
    public ResponseEntity<StockTransfer> getStockTransferById(@PathVariable("id") Long id) {
        Optional<StockTransfer> stockTransfer = stockTransferService.getStockTransferById(id);
        return stockTransfer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new stock transfer
    @PostMapping("/create")
    public ResponseEntity<StockTransfer> createStockTransfer(@RequestBody StockTransfer stockTransfer) {
        StockTransfer newStockTransfer = stockTransferService.saveStockTransfer(stockTransfer);
        return new ResponseEntity<>(newStockTransfer, HttpStatus.CREATED);
    }

    // Update an existing stock transfer
    @PutMapping("/update/{id}")
    public ResponseEntity<StockTransfer> updateStockTransfer(@PathVariable("id") Long id, @RequestBody StockTransfer updatedStockTransfer) {
        Optional<StockTransfer> stockTransferData = stockTransferService.getStockTransferById(id);
        if (stockTransferData.isPresent()) {
            StockTransfer existingStockTransfer = stockTransferData.get();
            existingStockTransfer.setFromLocation(updatedStockTransfer.getFromLocation());
            existingStockTransfer.setToLocation(updatedStockTransfer.getToLocation());
            StockTransfer savedStockTransfer = stockTransferService.saveStockTransfer(existingStockTransfer);
            return new ResponseEntity<>(savedStockTransfer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a stock transfer by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteStockTransfer(@PathVariable("id") Long id) {
        try {
            stockTransferService.deleteStockTransfer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
