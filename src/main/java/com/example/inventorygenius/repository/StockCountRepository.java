package com.example.inventorygenius.repository;

import com.example.inventorygenius.entity.StockCount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockCountRepository extends JpaRepository<StockCount, Long> {
    StockCount findByItem_SKUCode(String skuCode);
    // Query for stock counts with non-null items
    List<StockCount> findByItemIsNotNull();

    // Query for stock counts with non-null combos
    List<StockCount> findByComboIsNotNull();
}