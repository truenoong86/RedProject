package com.example.trueno.redproject.models;

public class Card {
    private String cardNo;
    private String cvvNo;
    private String expiryNo;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCvvNo() {
        return cvvNo;
    }

    public void setCvvNo(String cvvNo) {
        this.cvvNo = cvvNo;
    }

    public String getExpiryNo() {
        return expiryNo;
    }

    public void setExpiryNo(String expiryNo) {
        this.expiryNo = expiryNo;
    }

    public Card(String cardNo, String cvvNo, String expiryNo) {
        this.cardNo = cardNo;
        this.cvvNo = cvvNo;
        this.expiryNo = expiryNo;
    }



}
