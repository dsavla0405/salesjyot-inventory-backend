package com.example.inventorygenius.repository;

import com.example.inventorygenius.entity.StockCount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockCountRepository extends JpaRepository<StockCount, Long> {
    StockCount findByItem_SKUCodeAndUserEmail(String skuCode, String email);
    List<StockCount> findByItemIsNotNullAndUserEmail(String email);

    List<StockCount> findByComboIsNotNullAndUserEmail(String email);

    List<StockCount> findByUserEmail(String userEmail);
}