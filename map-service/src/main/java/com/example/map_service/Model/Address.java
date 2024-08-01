package com.example.map_service.Model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class Address {

    private Long id;
    private Long userId;
    private String receiverName;
    private String provinceCity; // Merged province and city into one field
    private String district;
    private String ward; // Added field for ward
    private String street;
    private Boolean isPrimary;
    private Double latitude;
    private Double longitude;

    // Getters and Setters
}
