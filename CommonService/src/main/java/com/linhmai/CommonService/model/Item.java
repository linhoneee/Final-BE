package com.linhmai.CommonService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private Integer weight;
    private String warehouseIds;
}