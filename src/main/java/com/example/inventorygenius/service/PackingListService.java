package com.example.inventorygenius.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.inventorygenius.entity.PackingList;
import com.example.inventorygenius.entity.Order;
import com.example.inventorygenius.entity.PickList;
import com.example.inventorygenius.entity.OrderData;
import com.example.inventorygenius.entity.PickListData;
import com.example.inventorygenius.service.OrderService;

import com.example.inventorygenius.repository.PackingListRepository;

@Service
public class PackingListService {

    @Autowired
    private PackingListRepository packingListRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PickListService pickListService;

    public List<PackingList> getAllPickLists() {
        return packingListRepository.findAll();
    }

    public Optional<PackingList> getPickListById(Long id) {
        return packingListRepository.findById(id);
    }

    public PackingList createPickList(PackingList pickList) {
        return packingListRepository.save(pickList);
    }

    public PackingList updatePickList(Long id, PackingList updatedPickList) {
        // Check if the picklist with the given id exists
        if (packingListRepository.existsById(id)) {
            updatedPickList.setPackinglistId(id);
            return packingListRepository.save(updatedPickList);
        } else {
            throw new RuntimeException("PickList not found with id: " + id);
        }
    }

    public void deletePickList(Long id) {
        // Check if the picklist with the given id exists
        if (packingListRepository.existsById(id)) {
            packingListRepository.deleteById(id);
        } else {
            throw new RuntimeException("PickList not found with id: " + id);
        }
    }

    public List<Order> getAllNotGeneratedPackListOrders() {
        List<Order> notGeneratedOrders = new ArrayList<>();
        List<PickList> allPickListOrders = pickListService.getAllPickLists();
        List<PackingList> allPackingListOrders = getAllPickLists(); // Assuming this method exists
        List<Order> packingListOrders = new ArrayList<>();
        for (PackingList pk : allPackingListOrders){
            for(Order oo : pk.getOrders()){
                packingListOrders.add(oo);
            }
        }

        for (PickList p : allPickListOrders) {
            for (Order o : p.getOrders()) {
                if (!packingListOrders.contains(o)) {
                    notGeneratedOrders.add(o);
                }
            }
        }

        return notGeneratedOrders;

    }

    public void deleteByPackingListNumber(Long packingListNumber){
        PackingList p = packingListRepository.findByPackingListNumber(packingListNumber);
        packingListRepository.delete(p);
    }
    
}

