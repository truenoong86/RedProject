package com.example.trueno.redproject.models;

public class Services {
    private String service;
    private String day_price;
    private String night_price;

    public Services(String service, String day_price , String night_price) {
        this.service = service;
        this.day_price = day_price;
        this.night_price = night_price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDay_price() {
        return day_price;
    }

    public void setDay_price(String day_price) {
        this.day_price = day_price;
    }

    public String getNight_price() {
        return night_price;
    }

    public void setNight_price(String night_price) {
        this.night_price = night_price;
    }

}
