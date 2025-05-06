package com.example.inventorygenius.repository;

import com.example.inventorygenius.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    // Custom query methods can be added here if needed
    List<StockTransfer> findByUserEmail(String userEmail);
}
