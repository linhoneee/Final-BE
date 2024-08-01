package com.example.map_service.entity;

import lombok.Data;

@Data
public class DistanceRecord {
    private Long userId;
    private String receiverName;
    private String provinceCity;
    private String district;
    private String ward;
    private String street;
    private double originLatitude;
    private double originLongitude;
    private Long warehouseId;
    private String warehouseName;
    private String warehouseProvinceCity;
    private String warehouseDistrict;
    private String warehouseWard;
    private double destinationLatitude;
    private double destinationLongitude;
    private double distance;
    private String route;
}
