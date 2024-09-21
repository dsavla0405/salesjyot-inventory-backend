package com.example.inventorygenius.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "location_name")
    private String locationName;

    // One-to-many mapping for stock transfers where this location is the fromLocation
    @JsonIgnore
    @OneToMany(mappedBy = "fromLocation")
    private List<StockTransfer> stockTransfersFrom;

    // One-to-many mapping for stock transfers where this location is the toLocation
    @JsonIgnore
    @OneToMany(mappedBy = "toLocation")
    private List<StockTransfer> stockTransfersTo;

    public Location(){

    }

    public Location(Long locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<StockTransfer> getStockTransfersFrom() {
        return stockTransfersFrom;
    }

    public void setStockTransfersFrom(List<StockTransfer> stockTransfersFrom) {
        this.stockTransfersFrom = stockTransfersFrom;
    }

    public List<StockTransfer> getStockTransfersTo() {
        return stockTransfersTo;
    }

    public void setStockTransfersTo(List<StockTransfer> stockTransfersTo) {
        this.stockTransfersTo = stockTransfersTo;
    }

}
