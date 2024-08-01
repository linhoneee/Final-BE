package com.example.DiscountService.DTO;

import lombok.Data;

@Data
public class ApplyCouponRequest {
    private String code;
    private Double orderValue;
    private Double shippingCost;
}
