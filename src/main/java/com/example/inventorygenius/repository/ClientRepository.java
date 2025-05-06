package com.example.inventorygenius.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventorygenius.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
}
