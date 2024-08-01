package com.example.InventoryService.Service;

import com.example.InventoryService.Entity.Inventory;
import com.example.InventoryService.Repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Flux<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Mono<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Mono<Inventory> saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Mono<Void> deleteInventory(Long id) {
        return inventoryRepository.deleteById(id);
    }
}