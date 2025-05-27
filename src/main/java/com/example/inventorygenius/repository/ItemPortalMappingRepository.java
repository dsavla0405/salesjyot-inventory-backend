package com.example.inventorygenius.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.inventorygenius.entity.ItemPortalMapping;
import java.util.List;
import java.util.Optional;


@Repository
public interface ItemPortalMappingRepository extends JpaRepository<ItemPortalMapping, Long> {
    ItemPortalMapping findByPortalAndPortalSkuCodeAndUserEmail(
    String portal, String portalSkuCode, String userEmail);
    List<ItemPortalMapping> findByUserEmail(String userEmail);
    Optional<ItemPortalMapping> findByIdAndUserEmail(Long id, String email);
}
