package com.example.inventorygenius.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findBySupplierNameAndUserEmail(String name, String email);
    Supplier findBySupplierNameAndPhonel(String supplierName, String phone);
    List<Supplier> findByUserEmail(String userEmail);
}
