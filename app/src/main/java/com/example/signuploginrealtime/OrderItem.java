package com.example.signuploginrealtime;

public class OrderItem {
    private String name;
    private double price;
    private int quantity;

    public OrderItem() {
        // Default constructor required for Firebase
    }

    public OrderItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

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
