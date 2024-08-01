package com.example.InventoryService.Repository;

import com.example.InventoryService.Entity.Inventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<Inventory, Long> {
}
