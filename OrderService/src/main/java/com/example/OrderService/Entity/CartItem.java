package com.example.OrderService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class CartItem {
    private int productId;
    private String name;
    private double price;
    private int quantity;

    // Getters and setters
}