package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.Combo;
import com.example.inventorygenius.entity.ComboItem;
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
            StockCount s = stockCountService.getStockCountBySKUCode(item.getItem().getSKUCode());
            Double prevCount = s.getCount();
            s.setCount(prevCount - item.getQuantityRequired());
            stockCountService.updateStockCount(s);

        }
        StockCount newStockCount = new StockCount();
        newStockCount.setCount(combo.getQtyToMake());
        newStockCount.setCombo(combo);
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
        if (comboRepository.existsById(id)) {
            comboRepository.deleteById(id);
            return true;
        }
        return false; // Combo not found
    }
}
