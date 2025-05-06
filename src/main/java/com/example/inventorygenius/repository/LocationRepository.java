package com.example.inventorygenius.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventorygenius.entity.Location;
import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLocationNameAndUserEmail(String locationName, String userEmail);
    List<Location> findByUserEmail(String userEmail);
}
