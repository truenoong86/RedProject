package com.example.trueno.redproject.models;

public class CardDetails {

    private String cvvNo;
    private String expiryNo;

    public CardDetails( String cvvNo, String expiryNo) {
        this.cvvNo = cvvNo;
        this.expiryNo = expiryNo;
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



}
