package com.example.ProfileCategoryService.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("products")
public class Product {
    @Id
    private Long id;

    @Column("product_name")
    private String productName;

    @Column("description_details")
    private String descriptionDetails;

    @Column("price")
    private Double price;

    @Column("category_id")
    private Long categoryId;

    @Column("weight")
    private Double weight;

    @Column("brand_id")
    private Long brandId;

    @Column("warehouse_ids")
    private String warehouseIds;

    // Getters and Setters
}
