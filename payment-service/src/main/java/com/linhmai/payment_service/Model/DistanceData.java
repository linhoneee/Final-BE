package com.linhmai.payment_service.Model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceData {
    @NotNull
    private Integer userId;

    private String originName;

    @NotNull
    private Double originLatitude;

    @NotNull
    private Double originLongitude;

    @NotNull
    private Integer warehouseId;

    private String destinationName;

    @NotNull
    private Double destinationLatitude;

    @NotNull
    private Double destinationLongitude;

    @NotNull
    private Double distance;

//    private String route;
}
