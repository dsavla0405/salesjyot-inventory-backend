package com.example.inventorygenius.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

@Entity
@Table(name = "stock_transfer") // Changed hyphen to underscore
public class StockTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_transfer_id")
    private Long stockTransferId;  

    @ManyToOne
    @JoinColumn(name = "from_location_id", referencedColumnName = "location_id")
    private Location fromLocation;

    @ManyToOne
    @JoinColumn(name = "to_location_id", referencedColumnName = "location_id")
    private Location toLocation;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "qty")
    private Double qty;

    public StockTransfer() {

    }

    public StockTransfer(Long stockTransferId, Location fromLocation, Location toLocation) {
        this.stockTransferId = stockTransferId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    public Long getStockTransferId() {
        return stockTransferId;
    }

    public void setStockTransferId(Long stockTransferId) {
        this.stockTransferId = stockTransferId;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
    
}
