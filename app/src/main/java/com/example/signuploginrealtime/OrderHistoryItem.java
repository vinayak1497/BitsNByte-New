package com.example.signuploginrealtime;

public class OrderHistoryItem {
    private String timestamp, status;
    private float totalAmount;

    public OrderHistoryItem() {
        // Default constructor required for Firebase
    }

    public OrderHistoryItem(String timestamp, float totalAmount, String status) {
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {return status;}
}
