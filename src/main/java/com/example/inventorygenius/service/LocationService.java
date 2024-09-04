package com.example.inventorygenius.service;

import com.example.inventorygenius.entity.Location;
import com.example.inventorygenius.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository; 

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long locationId) {
        return locationRepository.findById(locationId);
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long locationId, Location locationDetails) {
        return locationRepository.findById(locationId)
                .map(location -> {
                    location.setLocationName(locationDetails.getLocationName());
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id " + locationId));
    }

    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }
}
