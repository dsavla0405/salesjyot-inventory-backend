package com.example.inventorygenius.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventorygenius.entity.Return;
import java.util.List;


public interface ReturnRepository extends JpaRepository<Return, Long> {
    List<Return> findByUserEmail(String userEmail);
}