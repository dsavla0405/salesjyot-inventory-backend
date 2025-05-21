package com.example.inventorygenius.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.Storage;
import java.util.List;


@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findBySkucode(String skucode);
    Storage findByBinNumberAndRackNumberAndSkucode(String binNumber, String rackNumber, String skucode);
    boolean existsByItems_SKUCode(String skucode);
    List<Storage> findByUserEmail(String userEmail);
    
//    List<Storage> findByItems_ItemId(Long itemId);
    
    @Query("SELECT s FROM Storage s JOIN s.items i WHERE i.itemId = :itemId")
    List<Storage> findStorageByItemId(@Param("itemId") Long itemId);
}
