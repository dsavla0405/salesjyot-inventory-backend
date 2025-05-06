package com.example.inventorygenius.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.BomItem;
import com.example.inventorygenius.entity.Supplier;

@Repository
public interface BomItemRepository extends JpaRepository<BomItem, Long> {
        List<BomItem> findByUserEmail(String userEmail);

}
