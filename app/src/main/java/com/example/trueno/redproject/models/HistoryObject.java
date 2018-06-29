package com.example.trueno.redproject.models;

public class HistoryObject {

    private String rideId;
    private String passengerId;
    private String fromAddress;
    private String toAddress;
    private String rating;
    private String timestamp;

    public HistoryObject(String rideId, String passengerId, String fromAddress, String toAddress, String rating, String timestamp){
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.rating = rating;
        this.timestamp = timestamp;
    }


    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



}
