package com.example.signuploginrealtime;

public class OrderHistoryItem {
    private String timestamp, status;
    private float totalAmount;
    private String orderId;

    public OrderHistoryItem() {
        // Default constructor required for Firebase
    }

    public OrderHistoryItem(String timestamp, float totalAmount, String status, String orderId) {
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderId = orderId;

    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {return status;}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
