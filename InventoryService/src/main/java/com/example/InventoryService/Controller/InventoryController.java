package com.example.InventoryService.Controller;

import com.example.InventoryService.Entity.Inventory;
import com.example.InventoryService.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public Flux<Inventory> getAllInventories() {
        return inventoryService.getAllInventories();
    }

    @GetMapping("/{id}")
    public Mono<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id);
    }

    @PostMapping
    public Mono<Inventory> createInventory(@RequestBody Inventory inventory) {
        return inventoryService.saveInventory(inventory);
    }

    @PutMapping("/{id}")
    public Mono<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        return inventoryService.saveInventory(inventory);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteInventory(@PathVariable Long id) {
        return inventoryService.deleteInventory(id);
    }
}
