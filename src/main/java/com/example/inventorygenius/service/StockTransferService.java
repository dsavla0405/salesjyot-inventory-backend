package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.StockTransfer;
import com.example.inventorygenius.repository.StockTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockTransferService {

    @Autowired
    private StockTransferRepository stockTransferRepository;

    // Retrieve all stock transfers
    public List<StockTransfer> getAllStockTransfers() {
        return stockTransferRepository.findAll();
    }

    // Retrieve a single stock transfer by ID
    public Optional<StockTransfer> getStockTransferById(Long id) {
        return stockTransferRepository.findById(id);
    }

    // Create or update a stock transfer
    public StockTransfer saveStockTransfer(StockTransfer stockTransfer) {
        return stockTransferRepository.save(stockTransfer);
    }

    // Delete a stock transfer by ID
    public void deleteStockTransfer(Long id) {
        stockTransferRepository.deleteById(id);
    }
}
