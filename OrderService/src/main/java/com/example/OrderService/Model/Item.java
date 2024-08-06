package com.example.OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int productId;
    private String name;
    private double price;
    private int quantity;
    private double weight;
    private String warehouseIds;
    private String primaryImageUrl;
    private boolean isReviewed; // Thêm trường isReviewed
}
