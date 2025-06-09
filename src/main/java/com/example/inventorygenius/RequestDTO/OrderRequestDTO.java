package com.example.inventorygenius.RequestDTO;

import java.time.LocalDate;
import java.util.List;


public class OrderRequestDTO {

	
	private Long orderId;
    private LocalDate Date;
    private String orderNo;
    private String portal;
    private String portalOrderNo;
    private String portalOrderLineId;
    private List<ProductDTO> products;
    private LocalDate shipByDate;
    private String dispatched;
    private String courier;
    private String cancel;
    private String awbNo;
    private String orderStatus;    
    private String location;
    private String userEmail;
    
    
    
    public static class ProductDTO {
        private String portalSKU;
        private String skuCode;
        private String productDescription;
        private double quantity;
        
        
		public String getPortalSKU() {
			return portalSKU;
		}
		public void setPortalSKU(String portalSKU) {
			this.portalSKU = portalSKU;
		}
		
		public String getSkuCode() {
			return skuCode;
		}
		public void setSkuCode(String skuCode) {
			this.skuCode = skuCode;
		}
		public String getProductDescription() {
			return productDescription;
		}
		public void setProductDescription(String productDescription) {
			this.productDescription = productDescription;
		}
		public double getQuantity() {
			return quantity;
		}
		public void setQuantity(double quantity) {
			this.quantity = quantity;
		}
		@Override
		public String toString() {
			return "ProductDTO [portalSKU=" + portalSKU + ", skuCode=" + skuCode + ", productDescription="
					+ productDescription + ", quantity=" + quantity + "]";
		}
        
        
        
        }
    
    
    
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public LocalDate getDate() {
		return Date;
	}
	public void setDate(LocalDate date) {
		Date = date;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPortal() {
		return portal;
	}
	public void setPortal(String portal) {
		this.portal = portal;
	}
	public String getPortalOrderNo() {
		return portalOrderNo;
	}
	public void setPortalOrderNo(String portalOrderNo) {
		this.portalOrderNo = portalOrderNo;
	}
	public String getPortalOrderLineId() {
		return portalOrderLineId;
	}
	public void setPortalOrderLineId(String portalOrderLineId) {
		this.portalOrderLineId = portalOrderLineId;
	}
	
	public LocalDate getShipByDate() {
		return shipByDate;
	}
	public void setShipByDate(LocalDate shipByDate) {
		this.shipByDate = shipByDate;
	}
	public String getDispatched() {
		return dispatched;
	}
	public void setDispatched(String dispatched) {
		this.dispatched = dispatched;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getCancel() {
		return cancel;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	public String getAwbNo() {
		return awbNo;
	}
	public void setAwbNo(String awbNo) {
		this.awbNo = awbNo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	
	
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
	@Override
	public String toString() {
		return "OrderRequestDTO [orderId=" + orderId + ", Date=" + Date + ", orderNo=" + orderNo + ", portal=" + portal
				+ ", portalOrderNo=" + portalOrderNo + ", portalOrderLineId=" + portalOrderLineId + ", products="
				+ products + ", shipByDate=" + shipByDate + ", dispatched=" + dispatched + ", courier=" + courier
				+ ", cancel=" + cancel + ", awbNo=" + awbNo + ", orderStatus=" + orderStatus + ", location=" + location
				+ ", userEmail=" + userEmail + "]";
	}
    
    
    
    
    
}
