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
    private double currentLatitude;  // Tọa độ vĩ độ hiện tại của hàng hóa
    private double currentLongitude; // Tọa độ kinh độ hiện tại của hàng hóa

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }
}