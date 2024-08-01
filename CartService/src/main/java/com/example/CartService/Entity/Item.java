package com.example.CartService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Integer weight;
    private String warehouseIds;
    private String primaryImageUrl;

}
