package com.example.DiscountService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResult {
    private Double discountAmount;
    private String discountType;
    private Double discountedOrderValue;
    private Double discountedShippingCost;
}
