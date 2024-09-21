package com.example.inventorygenius.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "combo")
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comboId;

    @ManyToOne
    @JoinColumn(name = "item_id") // Foreign key to Item
    private Item comboName;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    private List<ComboItem> comboItems = new ArrayList<>();

    @Column(name = "qty_to_make")
    private Double qtyToMake;

    @OneToOne(mappedBy = "combo") // Mapped by field in StockCount
    @JsonIgnore
    private StockCount stockCount;

    public Combo(){
        
    }

    public Combo(Long comboId, Item comboName, List<ComboItem> comboItems, Double qtyToMake) {
        this.comboId = comboId;
        this.comboName = comboName;
        this.comboItems = comboItems;
        this.qtyToMake = qtyToMake;
    }

    public Long getComboId() {
        return comboId;
    }

    public void setComboId(Long comboId) {
        this.comboId = comboId;
    }

    public Item getComboName() {
        return comboName;
    }

    public void setComboName(Item comboName) {
        this.comboName = comboName;
    }

    public List<ComboItem> getComboItems() {
        return comboItems;
    }

    public void setComboItems(List<ComboItem> comboItems) {
        this.comboItems = comboItems;
    }

    public Double getQtyToMake() {
        return qtyToMake;
    }

    public void setQtyToMake(Double qtyToMake) {
        this.qtyToMake = qtyToMake;
    }

    public StockCount getStockCount() {
        return stockCount;
    }

    public void setStockCount(StockCount stockCount) {
        this.stockCount = stockCount;
    }
    
}

