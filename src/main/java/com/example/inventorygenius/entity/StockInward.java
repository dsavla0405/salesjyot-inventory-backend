package com.example.inventorygenius.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock-inward")
public class StockInward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockInwardId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "skucode")
    private String skucode;

    @Column(name = "qty")
    private String qty;

    @ManyToOne()
    @JoinColumn(name = "location_id")  // This is the foreign key column
    private Location location;  // Many StockInward records can be associated with one Location

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column (name = "user-email")
    private String userEmail;

    public StockInward() {}

    public StockInward(Long stockInwardId, LocalDate date, String skucode, String qty, Location location, String userEmail) {
        this.stockInwardId = stockInwardId;
        this.date = date;
        this.skucode = skucode;
        this.qty = qty;
        this.location = location;
        this.userEmail = userEmail;
    }

    public Long getStockInwardId() {
        return stockInwardId;
    }

    public void setStockInwardId(Long stockInwardId) {
        this.stockInwardId = stockInwardId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSkucode() {
        return skucode;
    }

    public void setSkucode(String skucode) {
        this.skucode = skucode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
