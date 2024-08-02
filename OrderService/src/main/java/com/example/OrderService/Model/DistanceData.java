package com.example.OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceData {
    private int userId;
    private String receiverName;
    private String provinceCity;
    private String district;
    private String ward;
    private String street;
    private double originLatitude;
    private double originLongitude;
    private int warehouseId;
    private String warehouseName;
    private String warehouseProvinceCity;
    private String warehouseDistrict;
    private String warehouseWard;
    private double destinationLatitude;
    private double destinationLongitude;
    private double distance;
}