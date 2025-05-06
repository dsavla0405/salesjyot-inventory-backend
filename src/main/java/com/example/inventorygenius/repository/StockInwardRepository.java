package com.example.inventorygenius.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.StockInward;
import java.util.List;


@Repository
public interface StockInwardRepository extends JpaRepository<StockInward, Long> {
    List<StockInward> findByUserEmail(String userEmail);
}
