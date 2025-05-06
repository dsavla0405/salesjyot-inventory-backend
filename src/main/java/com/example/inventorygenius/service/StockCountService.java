package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.entity.StockInward;
import com.example.inventorygenius.repository.StockCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockCountService {

    @Autowired
    StockCountRepository stockCountRepository;

    public StockCount saveStockCount(StockCount stockCount) {
        return stockCountRepository.save(stockCount);
    }

    // Method to retrieve a StockCount entity by its ID
    public Optional<StockCount> getStockCountById(Long id) {
        return stockCountRepository.findById(id);
    }

    // Method to retrieve all StockCount entities
    public List<StockCount> getAllStockCounts() {
        return stockCountRepository.findAll();
    }

    // Method to update a StockCount entity
    public StockCount updateStockCount(StockCount stockCount) {
        return stockCountRepository.save(stockCount);
    }

    // Method to delete a StockCount entity by its ID
    public void deleteStockCountById(Long id) {
        stockCountRepository.deleteById(id);
    }

    public StockCount getStockCountBySKUCode(String skuCode, String email) {
        return stockCountRepository.findByItem_SKUCodeAndUserEmail(skuCode, email);
    }

    // Fetch stock counts that are linked to Items
    public List<StockCount> getStockCountsByItem(String email) {
        return stockCountRepository.findByItemIsNotNullAndUserEmail(email);
    }

    // Fetch stock counts that are linked to Combos
    public List<StockCount> getStockCountsByCombo(String email) {
        return stockCountRepository.findByComboIsNotNullAndUserEmail(email);
    }

    public List<StockCount> getStockCountByUser(String email){
        return stockCountRepository.findByUserEmail(email);
    }
}