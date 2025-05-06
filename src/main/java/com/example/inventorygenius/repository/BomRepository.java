package com.example.inventorygenius.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.Supplier;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {
    Optional<Bom> findByBomCodeAndUserEmail(String bomCode, String userEmail);
    List<Bom> findByUserEmail(String userEmail);
}
