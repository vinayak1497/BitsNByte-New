package com.example.signuploginrealtime;

public class ContactModel {
    int img;              // Image resource ID
    String name;          // Food name
    String button;        // Button name or action (e.g., "Add to cart")
    String ingredients;   // Ingredients of the food item
    String price;         // Price of the food item

    // Constructor
    public ContactModel(int img, String name, String button, String ingredients, String price) {
        this.img = img;
        this.name = name;
        this.button = button;
        this.ingredients = ingredients;
        this.price = price;
    }

    // Getter methods
    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getButton() {
        return button;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getPrice() {
        return price;
    }
}
