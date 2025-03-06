package com.example.signuploginrealtime;

import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<CartItem> cartItems;  // List to store CartItems

    // Private constructor to ensure single instance
    private CartManager() {
        cartItems = new ArrayList<>();  // Initialize cartItems
    }

    // Singleton pattern to get the single instance of CartManager
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add a CartItem to the cart
    public void addToCart(String name, int quantity, float totalPrice) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();  // Ensure cartItems is initialized
        }
        cartItems.add(new CartItem(name, quantity, totalPrice));
    }

    // Get the list of CartItems
    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    // Calculate the grand total price of all items in the cart
    public float getGrandTotal() {
        float total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();  // Add each item's totalPrice
        }
        return total;
    }

    // **Clear the cart after successful order**
    public void clearCart() {
        cartItems.clear();  // Remove all items from the cart
    }
}
