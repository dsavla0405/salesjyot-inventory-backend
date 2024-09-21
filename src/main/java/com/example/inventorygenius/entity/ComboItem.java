package com.example.inventorygenius.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "combo_item")
public class ComboItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comboItemId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    //@JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item;

   // @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "bom_id")
    private Bom boms;

    private int quantity;

    private int quantityRequired;

    // Constructors, getters, and setters
    public ComboItem() {}

    public ComboItem(Long comboItemId, Combo combo, Item item, int quantity, int quantityRequired) {
        this.comboItemId = comboItemId;
        this.combo = combo;
        this.item = item;
        this.quantity = quantity;
        this.quantityRequired = quantityRequired;
    }

    public Long getComboItemId() {
        return comboItemId;
    }

    public void setComboItemId(Long comboItemId) {
        this.comboItemId = comboItemId;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Bom getBoms() {
        return boms;
    }

    public void setBoms(Bom boms) {
        this.boms = boms;
    }

    public int getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(int quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

}
