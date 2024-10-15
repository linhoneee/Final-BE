package com.linhmai.CommonService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceData {
    private Long userId;
    private String originName;
    private double originLatitude;
    private double originLongitude;
    private Long warehouseId;
    private String destinationName;
    private double destinationLatitude;
    private double destinationLongitude;
    private double distance;
    private String route;

}
