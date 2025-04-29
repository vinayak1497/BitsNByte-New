package com.example.signuploginrealtime;

public class OrderItem {
    private String name;
    private double price;
    private int quantity;

    // 🔥 Required empty constructor for Firebase
    public OrderItem() {
    }

    // ✅ Full constructor
    public OrderItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
