package com.example.map_service.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {

    private Long id;
    private String name;
    private String provinceCity;
    private String district;
    private String ward;
    private double latitude;
    private double longitude;
}
