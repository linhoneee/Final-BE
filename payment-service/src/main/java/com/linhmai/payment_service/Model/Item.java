package com.linhmai.payment_service.Model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotNull
    private Integer productId;

    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Integer quantity;

    private Integer weight;

    @NotNull
    private String warehouseIds;
}
