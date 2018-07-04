package com.example.trueno.redproject.models;

import java.io.Serializable;

public class User implements Serializable{
    private String name;
    private String countryCode;
    private String phone;
    private String email;
    private String vehicleNumber;
    private String vehicleMake;
    private String vehicleModel;
    private String tyreSize;
    private String insuranceCompany;
    private String yearOfManufacture;
    private String profileImageUrl;

    public User(){

    }

    public User(String name, String countryCode, String phone, String email, String vehicleNumber, String vehicleMake ,String vehicleModel, String tyreSize, String insuranceCompany, String yearOfManufacture) {
        this.name = name;
        this.countryCode = countryCode;
        this.phone = phone;
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.tyreSize = tyreSize;
        this.insuranceCompany = insuranceCompany;
        this.yearOfManufacture = yearOfManufacture;
        this.profileImageUrl = "No Image";
    }

    public User(String name, String countryCode, String phone, String email, String vehicleNumber, String vehicleMake ,String vehicleModel, String tyreSize, String insuranceCompany, String yearOfManufacture, String profileImageUrl) {
        this.name = name;
        this.countryCode = countryCode;
        this.phone = phone;
        this.email = email;
        this.vehicleNumber = vehicleNumber;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.tyreSize = tyreSize;
        this.insuranceCompany = insuranceCompany;
        this.yearOfManufacture = yearOfManufacture;
        this.profileImageUrl = profileImageUrl;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleMake(){ return vehicleMake;}

    public void setVehicleMake(String vehicleMake){this.vehicleMake = vehicleMake;}

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getTyreSize() {
        return tyreSize;
    }

    public void setTyreSize(String tyreSize) {
        this.tyreSize = tyreSize;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(String yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }




}
