package com.example.signuploginrealtime;

import java.util.List;

public class Order {
    private String orderId;
    private String moodleId;
    private String studentName;
    private List<OrderItem> items;
    private double totalAmount;
    private String timestamp;

    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderId, String moodleId, String studentName, List<OrderItem> items, double totalAmount, String timestamp) {
        this.orderId = orderId;
        this.moodleId = moodleId;
        this.studentName = studentName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getMoodleId() {
        return moodleId;
    }

    public String getStudentName() {
        return studentName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
