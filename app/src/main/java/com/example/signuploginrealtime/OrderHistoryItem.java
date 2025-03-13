package com.example.signuploginrealtime;

public class OrderHistoryItem {
    private String timestamp;
    private float totalAmount;

    public OrderHistoryItem() {
        // Default constructor required for Firebase
    }

    public OrderHistoryItem(String timestamp, float totalAmount) {
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getTotalAmount() {
        return totalAmount;
    }
}
