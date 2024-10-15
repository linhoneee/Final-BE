package com.linhmai.CommonService.model;

public class DistanceDataOrderService {
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

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProvinceCity() {
        return provinceCity;
    }

    public void setProvinceCity(String provinceCity) {
        this.provinceCity = provinceCity;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseProvinceCity() {
        return warehouseProvinceCity;
    }

    public void setWarehouseProvinceCity(String warehouseProvinceCity) {
        this.warehouseProvinceCity = warehouseProvinceCity;
    }

    public String getWarehouseDistrict() {
        return warehouseDistrict;
    }

    public void setWarehouseDistrict(String warehouseDistrict) {
        this.warehouseDistrict = warehouseDistrict;
    }

    public String getWarehouseWard() {
        return warehouseWard;
    }

    public void setWarehouseWard(String warehouseWard) {
        this.warehouseWard = warehouseWard;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
