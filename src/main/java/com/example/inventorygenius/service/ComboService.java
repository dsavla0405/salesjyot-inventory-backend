package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.Combo;
import com.example.inventorygenius.entity.ComboItem;
import com.example.inventorygenius.entity.Location;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.repository.ComboRepository;
import com.example.inventorygenius.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class ComboService {
    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StockCountService stockCountService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Combo createCombo(Combo combo) {
        for (ComboItem item : combo.getComboItems()) {
            item.setCombo(combo); // Ensure each ComboItem is linked to the Combo
            StockCount s = stockCountService.getStockCountBySKUCode(item.getItem().getSKUCode(), combo.getUserEmail());
            Double prevCount = s.getCount();
            s.setCount(prevCount - item.getQuantityRequired());
            stockCountService.updateStockCount(s);

        }
        StockCount newStockCount = new StockCount();
        newStockCount.setCount(combo.getQtyToMake());
        newStockCount.setCombo(combo);
        newStockCount.setUserEmail(combo.getUserEmail());
        stockCountService.saveStockCount(newStockCount);

        return comboRepository.save(combo);
    }

    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    public Combo getComboById(Long id) {
        Optional<Combo> optionalCombo = comboRepository.findById(id);
        return optionalCombo.orElse(null);
    }

    @Transactional
    public Combo updateCombo(Long id, Combo combo) {
        if (comboRepository.existsById(id)) {
            combo.setComboId(id); // Ensure the ID is set for the update
            for (ComboItem item : combo.getComboItems()) {
                item.setCombo(combo); // Link each ComboItem to the Combo
            }
            return comboRepository.save(combo);
        }
        return null; // Combo not found
    }

    @Transactional
    public boolean deleteCombo(Long id) {
        // Check if the combo exists
        if (comboRepository.existsById(id)) {
            // Fetch the combo to be deleted
            Combo combo = comboRepository.findById(id).orElseThrow(() -> new RuntimeException("Combo not found"));

            // Restore stock counts for each ComboItem
            for (ComboItem item : combo.getComboItems()) {
                StockCount stockCount = stockCountService.getStockCountBySKUCode(item.getItem().getSKUCode(), combo.getUserEmail());
                if (stockCount != null) {
                    // Increase the stock count by the quantity required
                    Double updatedCount = stockCount.getCount() + item.getQuantityRequired();
                    stockCount.setCount(updatedCount);
                    stockCountService.updateStockCount(stockCount);
                }
            }

            // Delete the combo
            comboRepository.deleteById(id);
            return true; // Deletion successful
        }
        return false; // Combo not found
    }

    public List<Combo> getComboByUser(String email){
        return comboRepository.findByUserEmail(email);
    }

}
