package com.linhmai.payment_service.Model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @NotNull
    private Long id;

    @NotNull
    private Integer userId;

    @NotNull
    private List<Item> items;

    @NotNull
    private Shipping selectedShipping;

    @NotNull
    private DistanceData distanceData;

    @NotNull
    private Double total;

    private String platform; // Thêm thuộc tính platform
}
