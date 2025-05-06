package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Optional<Item> getItemById(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public Item updateItem(Long id, Item updatedItem) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        
        existingItem.setSKUCode(updatedItem.getSKUCode());
        existingItem.setDescription(updatedItem.getDescription());
            existingItem.setPackOf(updatedItem.getPackOf());
            existingItem.setParentSKU(updatedItem.getParentSKU());
            existingItem.setBarcode(updatedItem.getBarcode());
            existingItem.setSKUCode(updatedItem.getSKUCode());
            existingItem.setGroup1(updatedItem.getGroup1());
            existingItem.setGroup2(updatedItem.getGroup2());
            existingItem.setGroup3(updatedItem.getGroup3());
            existingItem.setSizeRange(updatedItem.getSizeRange());
            existingItem.setUnit(updatedItem.getUnit());
            existingItem.setSellerSKUCode(updatedItem.getSellerSKUCode());
            existingItem.setSellingPrice(updatedItem.getSellingPrice());
            existingItem.setMrp(updatedItem.getMrp());
            existingItem.setImg(updatedItem.getImg());
            existingItem.setSuppliers(updatedItem.getSuppliers());
            existingItem.setStorages(updatedItem.getStorages());
            existingItem.setBoms(updatedItem.getBoms());
            existingItem.setStockEntries(updatedItem.getStockEntries());
            existingItem.setOrders(updatedItem.getOrders());
        
        return itemRepository.save(existingItem);
    }

    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
