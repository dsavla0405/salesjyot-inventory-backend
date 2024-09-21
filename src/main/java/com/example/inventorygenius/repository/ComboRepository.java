package com.example.inventorygenius.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.Combo;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {

}

