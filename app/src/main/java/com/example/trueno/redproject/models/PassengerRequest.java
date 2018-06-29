package com.example.trueno.redproject.models;

public class PassengerRequest {
    private String name;
    private String vehicleNumber;
    private String vehicleModel;
    private String pickupName;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String destinationName;
    private String serviceType;
    private Double price;

    public PassengerRequest() {

    }

    public PassengerRequest(String name, String vehicleNumber, String vehicleModel, String pickupName, Double pickupLatitude, Double pickupLongitude, String destinationName, String serviceType, Double price) {
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.pickupName = pickupName;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationName = destinationName;
        this.serviceType = serviceType;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
