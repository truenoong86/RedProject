package com.example.trueno.redproject.models;


public class Feedback {
    private String title;
    private String message;
    private String date;
    private String role;


    public Feedback(String title, String message,  String date, String role) {
        this.title = title;
        this.message = message;
        this.date = date;
        this.role = role;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}
