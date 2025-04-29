package com.example.signuploginrealtime;

import java.util.List;

public class Order {
    private String orderId;
    private String studentName;
    private double totalAmount;
    private String timestamp;
    private String status;
    private List<OrderItem> items;
    private String userId;

    public Order() {
    }

    // ✅ Full constructor
    public Order(String orderId, String studentName, List<OrderItem> items, double totalAmount, String timestamp, String status) {
        this.orderId = orderId;
        this.studentName = studentName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.status = status;
        this.userId = userId;

    }

    // Getters
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getStudentName() {
        return studentName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
