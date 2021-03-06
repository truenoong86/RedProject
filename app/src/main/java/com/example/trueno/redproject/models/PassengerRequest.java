package com.example.trueno.redproject.models;

public class PassengerRequest {
    private String name;
    private String vehicleNumber;
    private String vehicleModel;
    private String pickupName;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String destinationName;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String serviceType;
    private String status;
    private Double price;
    private String remarks;
    private String promo;
    private String passengeruid;

    public PassengerRequest() {

    }

    public PassengerRequest(String name, String vehicleNumber, String vehicleModel, String pickupName, Double pickupLatitude, Double pickupLongitude, String destinationName, Double destinationLatitude, Double destinationLongitude, String serviceType, String status, Double price, String remarks, String promo, String passengeruid) {
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.pickupName = pickupName;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.destinationName = destinationName;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.serviceType = serviceType;
        this.status = status;
        this.price = price;
        this.remarks = remarks;
        this.promo = promo;
        this.passengeruid = passengeruid;
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

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getPassengeruid() {
        return passengeruid;
    }

    public void setPassengeruid(String passengeruid) {
        this.passengeruid = passengeruid;
    }
}
