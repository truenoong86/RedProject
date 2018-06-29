package com.example.trueno.redproject.models;

public class PassengerRequest {
    private String name;
    private String vehicleNumber;
    private String vehicleModel;
    private String pickupName;
    private String destinationName;

    public PassengerRequest() {

    }

    public PassengerRequest(String name, String vehicleNumber, String vehicleModel, String pickupName, String destinationName) {
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.pickupName = pickupName;
        this.destinationName = destinationName;
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

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
