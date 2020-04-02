package com.example.assignment_5;

public class Transaction {
    String date, reason;
    Float amount;
    public Transaction(){

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Transaction(String date_in, Float amount_in, String reason_in) {
        this.date = date_in;
        this.amount = amount_in;
        this.reason = reason_in;
    }
}
