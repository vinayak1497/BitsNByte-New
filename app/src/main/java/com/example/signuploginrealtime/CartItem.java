package com.example.signuploginrealtime;

public class CartItem {
    private String name;
    private int quantity;
    private float totalPrice;

    // Constructor for creating CartItem
    public CartItem(String name, int quantity, float totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Getter for totalPrice
    public float getTotalPrice() {
        return totalPrice;
    }
}
