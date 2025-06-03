package com.example.inventorygenius.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorygenius.entity.Storage;
import com.example.inventorygenius.service.StorageService;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping
    public ResponseEntity<Storage> addStorage(@RequestBody Storage storage) {
        Storage newStorage = storageService.addStorage(storage);
        return new ResponseEntity<>(newStorage, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{itemId}", consumes = { "application/json" })
    public ResponseEntity<Storage> addStorageWithItem(@RequestBody Storage storage, @PathVariable Long itemId) {
        Storage newStorage = storageService.addStorageWithItem(storage, itemId);
        if (newStorage != null) {
            return new ResponseEntity<>(newStorage, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Storage> updateStorage(@PathVariable Long id, @RequestBody Storage storageDetails) {
        Storage updatedStorage = storageService.updateStorage(id, storageDetails);
        return new ResponseEntity<>(updatedStorage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorage() {
        List<Storage> storage = storageService.getAllStorage();
        return new ResponseEntity<>(storage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteStorage(@PathVariable("id") Long id) {
        System.out.println("deleted");
        storageService.deleteStorageById(id);
    }

    @GetMapping("/user/email")
    public List<Storage> getStorageByUser(@RequestParam String email) {
        return storageService.getStorageByUser(email);
    }
    
    @GetMapping("/{id}")
    public List<Storage>getStorageByItemId(@PathVariable("id") Long id){
    	
    	return storageService.getStorageByItemId(id);
    }
    
//    @PostMapping("/viaStockTransfer")
//    public
}
