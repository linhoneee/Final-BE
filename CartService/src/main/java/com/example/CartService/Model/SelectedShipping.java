package com.example.CartService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedShipping {
    private int id;
    private String name;
    private double pricePerKm;
    private double pricePerKg;
}
