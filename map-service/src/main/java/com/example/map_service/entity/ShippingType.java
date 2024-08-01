package com.example.map_service.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("shipping_type")
public class ShippingType {
    @Id
    private Long id;
    private String name;
    private double pricePerKm;
    private double pricePerKg;
}
