package com.example.inventorygenius.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.inventorygenius.RequestDTO.OrderRequestDTO;
import com.example.inventorygenius.RequestDTO.OrderRequestDTO.ProductDTO;
import com.example.inventorygenius.entity.Bom;
import com.example.inventorygenius.entity.BomItem;
import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.ItemPortalMapping;
import com.example.inventorygenius.entity.Location;
import com.example.inventorygenius.entity.Order;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockCount;
import com.example.inventorygenius.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;
    
    @Autowired 
    private StockCountService stockCountService;
    
    @Autowired
    private LocationService locationService;
    
    @Autowired
    private ItemSupplierService itemSupplierService;
    
    @Autowired
    private ItemPortalMappingService ipmService;

    public Order addOrder(Order order) {
    List<Item> newItems = new ArrayList<>();
    for (Item item : order.getItems()) {
        if (item.getItemId() == null) { 
            newItems.add(item);
        }
        if (order.getCancel() == null || order.getCancel().equals("")) {
            order.setCancel("Order Not Canceled");
        }
        newItems.add(item);
    }

    if (order.getSkucode() == null || order.getSkucode().equals("")) {
        order.setSkucode(order.getItemPortalMapping().getSkucode());
    }

    if (order.getShipByDate() == null || order.getShipByDate().equals("")) {
        order.setShipByDate(LocalDate.now());
    }

    return orderRepository.save(order);
}

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
    }

    public void deleteOrderById(Long id) {
        Order order = findById(id);
        Stock stock = new Stock();
        stock.setDate(order.getDate());
        stock.setSkucode(order.getItems().get(0).getSKUCode());
        stock.setAddQty(String.valueOf(order.getQty()));
        stock.setSubQty("0");
        stock.setItem(order.getItems().get(0));
        stock.setLocation(order.getLocation());
        stock.setSource("order");
        stock.setMessage("order deleted");
        stock.setNumber("order no = " + String.valueOf(order.getOrderNo()));

        stockService.addStock(stock);
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long orderId, Order updatedOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();

            existingOrder.setDate(updatedOrder.getDate());
            existingOrder.setOrderNo(updatedOrder.getOrderNo());
            existingOrder.setPortal(updatedOrder.getPortal());
            existingOrder.setPortalOrderNo(updatedOrder.getPortalOrderNo());
            existingOrder.setPortalOrderLineId(updatedOrder.getPortalOrderLineId());
            existingOrder.setPortalSKU(updatedOrder.getPortalSKU());
            existingOrder.setSkucode(updatedOrder.getSkucode());
            existingOrder.setProductDescription(updatedOrder.getProductDescription());
            existingOrder.setQty(updatedOrder.getQty());
            existingOrder.setShipByDate(updatedOrder.getShipByDate());
            existingOrder.setDispatched(updatedOrder.getDispatched());
            existingOrder.setCourier(updatedOrder.getCourier());
            existingOrder.setCancel(updatedOrder.getCancel());
            existingOrder.setItems(updatedOrder.getItems());
            existingOrder.setAwbNo(updatedOrder.getAwbNo());
            existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
            existingOrder.setItemPortalMapping(updatedOrder.getItemPortalMapping());
            existingOrder.setPicklist(updatedOrder.getPicklist());
            
            return orderRepository.save(existingOrder);
        } else {
            return null;
        }
    }

    public Order findByOrderNo(String orderNo, String email) {
        return orderRepository.findByOrderNoAndUserEmail(orderNo, email);
    }

    public List<Order> findNotDispatchedOrders(){
        List<Order> orders = getAllOrders();
        List<Order> notDispatchedOrders = new ArrayList<>();

        for (Order o : orders) {
            if (!o.getOrderStatus().equals("dispatched")){
                notDispatchedOrders.add(o);
            }
        }
        return notDispatchedOrders;
    }
    
    public List<Order> findNotPackedOrders(String email){
        List<Order> orders = getOrdersByUser(email);
        List<Order> notPackedOrders = new ArrayList<>();

        for (Order o : orders) {
            if (!o.getOrderStatus().equals("dispatched") && !o.getOrderStatus().equals("packed")){
                notPackedOrders.add(o);
            }
        }
        return notPackedOrders;
    }

    public List<Order> findByAwbNo(String AwbNo){
        return orderRepository.findByAwbNo(AwbNo);
    }

    public List<Order> findOrdersByLocation(Location location, String email){
        return orderRepository.findByLocationAndUserEmail(location, email);
    }

    public List<Order> getOrdersByUser(String email){
        return orderRepository.findByUserEmail(email);
    }

    public List<Order> addnewOrder(OrderRequestDTO orderDto) {
    	
    	
        List<Order> orders = new ArrayList<>();
        

        if (orderDto.getProducts().get(0).getPortalSKU().isEmpty()) {
            throw new NoSuchElementException("No products added.");
        }

        String email = orderDto.getUserEmail();
        String portal = orderDto.getPortal();
        Location location = locationService.findByName(orderDto.getLocation(), email);

        for (ProductDTO product : orderDto.getProducts()) {

            Item item = itemSupplierService.findItemsBySellerSKUAndDescription(
                product.getSkuCode(), product.getProductDescription(), email
            );

            if (item == null) {
                throw new NoSuchElementException("Item not found for SKU: " + product.getSkuCode());
            }

            ItemPortalMapping ipm = ipmService.getItemPortalMappings(portal, product.getPortalSKU(), email);

            Order order = new Order();
            order.setDate(orderDto.getDate());
            order.setOrderNo(orderDto.getOrderNo());
            order.setPortal(portal);
            order.setPortalOrderNo(orderDto.getPortalOrderNo());
            order.setPortalOrderLineId(orderDto.getPortalOrderLineId());
            order.setAwbNo(orderDto.getAwbNo());
            order.setDispatched(orderDto.getDispatched() ); // If it's boolean
            order.setCourier(orderDto.getCourier());
            
            if (orderDto.getCancel() == null || orderDto.getCancel().equals("")) {
                order.setCancel("Order Not Canceled");
            }
            
            order.setOrderStatus(orderDto.getOrderStatus());
            order.setShipByDate(orderDto.getShipByDate());

            // Product-specific
            order.setPortalSKU(product.getPortalSKU());
            order.setSkucode(product.getSkuCode());
            order.setProductDescription(product.getProductDescription());
            order.setQty(product.getQuantity());

            // Relational
            order.setUserEmail(email);
            order.setLocation(location);
            order.setItemPortalMapping(ipm);
            order.setItems(Collections.singletonList(item));
            
            

            orders.add(orderRepository.save(order));
        }

        return orders;
    }
    
    public ResponseEntity<Order> editOrder(Long orderId,OrderRequestDTO UpdatedOrderDto) {

        System.out.println("Received request to update order with ID: " + orderId);

        Optional<Order> existingOrderOptional = orderRepository.findById(orderId);

        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            System.out.println("Existing order found: " + existingOrder);

            // Update properties of the existing item with the new values
            existingOrder.setCourier(UpdatedOrderDto.getCourier());
            existingOrder.setDate(UpdatedOrderDto.getDate());
            existingOrder.setDispatched(UpdatedOrderDto.getDispatched());
            existingOrder.setOrderNo(UpdatedOrderDto.getOrderNo());
            existingOrder.setPortal(UpdatedOrderDto.getPortal());
            existingOrder.setPortalOrderLineId(UpdatedOrderDto.getPortalOrderLineId());
            existingOrder.setPortalOrderNo(UpdatedOrderDto.getPortalOrderNo());

            existingOrder.setShipByDate(UpdatedOrderDto.getShipByDate());

            existingOrder.setCancel(UpdatedOrderDto.getCancel());
            existingOrder.setAwbNo(UpdatedOrderDto.getAwbNo());
            existingOrder.setOrderStatus(UpdatedOrderDto.getOrderStatus());
            
            
            Location location = locationService.findByName(UpdatedOrderDto.getLocation(), UpdatedOrderDto.getUserEmail());
            
            existingOrder.setLocation(location);
            
            ProductDTO product = UpdatedOrderDto.getProducts().get(0);
            Item item = itemSupplierService.findItemsBySellerSKUAndDescription(
                    product.getSkuCode(),
                    product.getProductDescription(),
                    UpdatedOrderDto.getUserEmail()
                );

            if(item == null) {
                System.out.println("Item is null for SKUCode: " + product.getSkuCode());
            } else {
                System.out.println("Item found: " + item);
            }
//            existingOrder.setItems(List.of(item));
            existingOrder.setItems(new ArrayList<>(List.of(item)));

            existingOrder.setQty(product.getQuantity());
            
            System.out.println("Updated order: " + existingOrder);
            
            if (UpdatedOrderDto.getCancel().equals("Order Canceled") && existingOrder.getPicklist() == null) {
                System.out.println("Order is canceled. Updating stock.");
                Stock stock = new Stock();               
//                Item item = itemSupplierService.getItemBySKUCode(UpdatedOrderDto.getSKUCode());

                

                if (item != null && item.getBoms().size() > 0) {
                    stock.setDate(LocalDate.now());
                    stock.setSkucode(item.getParentSKU());
                    stock.setSubQty("0");

                    for (Bom bom : item.getBoms()) {
                        for (BomItem bomItem : bom.getItemsInBom()) {
                            if (bomItem.getItem().getSKUCode().equals(item.getParentSKU())) {
                                stock.setAddQty(String.valueOf(product.getQuantity() * Double.parseDouble(bomItem.getQty())));
                            } else {
                                Stock s = new Stock();
                                stock.setDate(LocalDate.now());
                                s.setSubQty("0");
                                s.setItem(item);
                                s.setAddQty(String.valueOf(product.getQuantity() * Double.parseDouble(bomItem.getQty())));
                                s.setSkucode(bomItem.getBomItem());
                                s.setSource("Order");
                                s.setMessage("Order Cancelled");
                                s.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));
                                s.setLocation(location);
                                System.out.println("Adding stock for BOM item: " + s);
                                stockService.addStock(s);
                            }
                        }
                    }

//                    stock.setItem(UpdatedOrderDto.getItems().get(0));
                    stock.setItem(item);
                    stock.setSource("Order");
                    stock.setMessage("Order Cancelled");
                    stock.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));
                    stock.setLocation(location);
                } else {
                    stock.setDate(LocalDate.now());
//                    stock.setSkucode(UpdatedOrderDto.getItems().get(0).getSKUCode());
                    stock.setSkucode(item.getSKUCode());
                    stock.setSubQty("0");
                    stock.setAddQty(String.valueOf(product.getQuantity()));
//                    stock.setItem(UpdatedOrderDto.getItems().get(0));
                    stock.setItem(item);
                    stock.setSource("Order");
                    stock.setLocation(location);
                    stock.setMessage("Order Cancelled");
                    stock.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));
                }

                System.out.println("Adding stock: " + stock);
                stockService.addStock(stock);

                StockCount sc = new StockCount();
                if (item.getBoms().size() > 0) {
                    for (Bom b : item.getBoms()) {
                        for (BomItem bomItem : b.getItemsInBom()) {
                            if (bomItem.getItem().getSKUCode().equals(item.getParentSKU())) {
                                sc = stockCountService.getStockCountBySKUCode(item.getParentSKU(), UpdatedOrderDto.getUserEmail());
                                sc.setCount(sc.getCount() + product.getQuantity() * Double.parseDouble(bomItem.getQty()));
                            } else {
                                StockCount scBom = stockCountService.getStockCountBySKUCode(bomItem.getItem().getSKUCode(), UpdatedOrderDto.getUserEmail());
                                scBom.setCount(scBom.getCount() + product.getQuantity() * Double.parseDouble(bomItem.getQty()));
                                System.out.println("Updating stock count for BOM item: " + scBom);
                                stockCountService.updateStockCount(scBom);
                            }
                        }
                    }
                } else {
                    sc = stockCountService.getStockCountBySKUCode(item.getSKUCode(), UpdatedOrderDto.getUserEmail());
                    sc.setCount(sc.getCount() + product.getQuantity());
                }
                System.out.println("Updating stock count1: " + sc);
                stockCountService.updateStockCount(sc);
            }

            if (UpdatedOrderDto.getCancel().equals("Order Not Canceled")) {
                System.out.println("Order is not canceled. Updating stock.");
                Stock stock = new Stock();
//                Item item = itemSupplierService.getItemBySKUCode(UpdatedOrderDto.getItems().get(0).getSKUCode());

               

                if (item != null && item.getBoms().size() > 0) {
                    stock.setDate(LocalDate.now());
                    stock.setSkucode(item.getParentSKU());
                    stock.setAddQty("0");

                    for (Bom bom : item.getBoms()) {
                        for (BomItem bomItem : bom.getItemsInBom()) {
                            if (bomItem.getBomItem().equals(item.getParentSKU())) {
                                stock.setSubQty(String.valueOf(product.getQuantity() * Double.parseDouble(bomItem.getQty())));
                            } else {
                                Stock s = new Stock();
                                s.setDate(LocalDate.now());
                                s.setAddQty("0");
                                s.setItem(item);
                                s.setSubQty(String.valueOf(product.getQuantity() * Double.parseDouble(bomItem.getQty())));
                                s.setSkucode(bomItem.getBomItem());
                                s.setSource("Order");
                                s.setLocation(location);
                                s.setMessage("Order Not Cancelled");
                                s.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));

                                System.out.println("Adding stock for BOM item: " + s);
                                stockService.addStock(s);
                            }
                        }
                    }

                    stock.setItem(item);
                    stock.setSource("Order");
                    stock.setLocation(location);
                    stock.setMessage("Order Not Cancelled");
                    stock.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));
                } else {
                    stock.setDate(LocalDate.now());
                    stock.setSkucode(item.getSKUCode());
                    stock.setAddQty("0");
                    stock.setSubQty(String.valueOf(product.getQuantity()));
                    stock.setItem(item);
                    stock.setSource("Order");
                    stock.setLocation(location);
                    stock.setMessage("Order Not Cancelled");
                    stock.setNumber("Order Number = " + String.valueOf(UpdatedOrderDto.getOrderNo()));
                }

                System.out.println("Adding stock: " + stock);
                stockService.addStock(stock);

                // StockCount sc = stockCountService.getStockCountBySKUCode(UpdatedOrderDto.getItems().get(0).getSKUCode());
                // System.out.println("stock count sku = " + sc.getItem().getSKUCode());
                // if (UpdatedOrderDto.getItems().get(0).getBoms().size() > 0) {
                //     for (Bom b : UpdatedOrderDto.getItems().get(0).getBoms()) {
                //         for (BomItem bomItem : b.getItemsInBom()) {
                //             if (bomItem.getBomItem().equals(UpdatedOrderDto.getItems().get(0).getParentSKU())) {
                //                 sc = stockCountService.getStockCountBySKUCode(UpdatedOrderDto.getItems().get(0).getParentSKU());
                //                 if (existingOrder.getQty() != UpdatedOrderDto.getQty()) {
                //                     sc.setCount(sc.getCount() + UpdatedOrderDto.getQty() * Double.parseDouble(bomItem.getQty()));
                //                 }
                //             } else {
                //                 StockCount scBom = stockCountService.getStockCountBySKUCode(bomItem.getBomItem());
                //                 if (existingOrder.getQty() != UpdatedOrderDto.getQty()) {
                //                     scBom.setCount(scBom.getCount() + UpdatedOrderDto.getQty() * Double.parseDouble(bomItem.getQty()));
                //                 }
                //                 System.out.println("Updating stock count for BOM item: " + scBom);
                //                 stockCountService.updateStockCount(scBom);
                //             }
                //         }
                //     }
                // }
                // System.out.println("Updating stock count2: " + sc);
                // stockCountService.updateStockCount(sc);
            }

            // Save the updated item
            System.out.println("Saving updated order: " + existingOrder);
            
            Order savedOrder = orderRepository.save(existingOrder);
            
//            System.out.println("Updated order saved: " + savedOrder);
            return ResponseEntity.ok(savedOrder);
            
            
        } else {
            System.out.println("Order not found for ID: " + orderId);
            return ResponseEntity.notFound().build();
        }
		
    }
    
	public String setNewOrderNo(String email) {

		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		String formattedDate = today.format(formatter);

		List<Order> o = orderRepository.findByUserEmail(email);

		String padding;
		if (o.isEmpty()) 
			padding = "0001";
		 else {
			String prevOrderNo = o.get(o.size() - 1).getOrderNo();

			int prevSerial = Integer.valueOf(prevOrderNo.substring(prevOrderNo.lastIndexOf("-") + 1));

			int newSerial = prevSerial + 1;

			padding = String.format("%04d", newSerial);
		}

		return (formattedDate + "-" + padding);


	}

}
