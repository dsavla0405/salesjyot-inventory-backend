package com.example.inventorygenius.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.StockTransfer;
import com.example.inventorygenius.entity.Storage;
import com.example.inventorygenius.repository.StockTransferRepository;
import com.example.inventorygenius.repository.StorageRepository;

import jakarta.transaction.Transactional;

@Service
public class StockTransferService {

    @Autowired
    private StockTransferRepository stockTransferRepository;
    
    @Autowired
    private StorageRepository storageRepo;

    // Retrieve all stock transfers
    public List<StockTransfer> getAllStockTransfers() {
        return stockTransferRepository.findAll();
    }

    // Retrieve a single stock transfer by ID
    public Optional<StockTransfer> getStockTransferById(Long id) {
        return stockTransferRepository.findById(id);
    }

    // Create or update a stock transfer
    @Transactional
    public StockTransfer saveStockTransfer(StockTransfer stockTransfer) {
    	
    	
    	Storage StorageData = storageRepo.findById(stockTransfer.getFromStorage().getStorageId())
    	        .orElseThrow(() -> new RuntimeException("Storage not found"));

    	    double currentQty = Double.parseDouble(StorageData.getQty());
    	    
    	    if (stockTransfer.getQty() > currentQty) {
    	        throw new IllegalArgumentException("Transfer quantity exceeds available quantity");
    	    }

    	    if (stockTransfer.getQty() == currentQty) {
    	    	
    	    	StorageData.setLocation(stockTransfer.getToLocation());
    	        storageRepo.save(StorageData);
    	    }
    	    else {
    	        // Case 2: Partial transfer
    	        int remainingQty =(int) (currentQty - stockTransfer.getQty());
    	        System.out.println("remainingQty::::"+remainingQty);
    	        StorageData.setQty(String.valueOf(remainingQty));
    	        storageRepo.save(StorageData);
    	        
    	        List<Item> copiedItems = new ArrayList<>(StorageData.getItems());
    	        Storage NewStorage = new Storage();
    	        NewStorage.setQty(String.valueOf((stockTransfer.getQty()).intValue()));
    	        NewStorage.setSkucode(stockTransfer.getItem().getSKUCode());
    	        NewStorage.setItems(copiedItems);
    	        NewStorage.setRackNumber("tbd"); // from input
    	        NewStorage.setBinNumber("tbd");  // from input
    	        NewStorage.setLocation(stockTransfer.getToLocation());
    	        NewStorage.setUserEmail(stockTransfer.getUserEmail());
    	        storageRepo.save(NewStorage);
    	    }
        return stockTransferRepository.save(stockTransfer);
    }

    public void deleteStockTransfer(Long id) {
        stockTransferRepository.deleteById(id);
    }

    public List<StockTransfer> getStockTransferByUser(String email){
        return stockTransferRepository.findByUserEmail(email);
    }
}
