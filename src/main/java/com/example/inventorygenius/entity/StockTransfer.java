package com.example.inventorygenius.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    @Column (name = "user-email")
    private String userEmail;
    
    @ManyToOne
    @JoinColumn(name = "from_storage_id", referencedColumnName = "storage_id", nullable = true)
    private Storage fromStorage;

    public StockTransfer() {

    }

   

    public StockTransfer(Long stockTransferId, Location fromLocation, Location toLocation, Item item, Double qty,
			String userEmail, Storage fromStorage) {
		this.stockTransferId = stockTransferId;
		this.fromLocation = fromLocation;
		this.toLocation = toLocation;
		this.item = item;
		this.qty = qty;
		this.userEmail = userEmail;
		this.fromStorage = fromStorage;
	}

	public Storage getFromStorage() {
		return fromStorage;
	}

	public void setFromStorage(Storage fromStorage) {
		this.fromStorage = fromStorage;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



	@Override
	public String toString() {
		return "StockTransfer [stockTransferId=" + stockTransferId + ", fromLocation=" + fromLocation + ", toLocation="
				+ toLocation + ", item=" + item + ", qty=" + qty + ", userEmail=" + userEmail + ", fromStorage="
				+ fromStorage + "]";
	}
    
}
