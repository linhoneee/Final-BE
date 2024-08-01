package com.linhmai.payment_service.Model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipping {
    @NotNull
    private Integer id;

    private String name;

    @NotNull
    private Double pricePerKm;

    @NotNull
    private Double pricePerKg;
}
