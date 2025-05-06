package com.example.inventorygenius.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventorygenius.entity.Item;
import com.example.inventorygenius.entity.Location;
import com.example.inventorygenius.entity.Order;
import com.example.inventorygenius.entity.PackingList;
import com.example.inventorygenius.entity.PickList;
import com.example.inventorygenius.entity.Stock;
import com.example.inventorygenius.entity.StockInward;
import com.example.inventorygenius.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockService stockService;

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

    public List<Order> findByOrderNo(String orderNo, String email) {
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
}
