package com.example.signuploginrealtime;
import android.annotation.SuppressLint;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Description_Activity extends AppCompatActivity {

    private ImageView foodImage, backButton;
    private TextView foodName, foodIngredients, foodPrice, quantityTextView, incrementButton, decrementButton;
    private Button addToCartButton;

    int quantity = 1; // Initial quantity
    float itemPrice;  // Updated to numeric type for better calculations

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_activity);

        // Initialize views
        foodImage = findViewById(R.id.food_item_image);
        foodName = findViewById(R.id.food_item_name);
        foodIngredients = findViewById(R.id.ingredients_name);
        foodPrice = findViewById(R.id.price_name);
        quantityTextView = findViewById(R.id.value);
        addToCartButton = findViewById(R.id.add_to_cart_btn);
        incrementButton = findViewById(R.id.plus_btn);
        decrementButton = findViewById(R.id.minus_btn);
        backButton = findViewById(R.id.back_btn);

        // Set back button functionality
        backButton.setOnClickListener(v -> finish());

        // Get intent data
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String ingredients = intent.getStringExtra("ingredientsId");
        int imageResId = intent.getIntExtra("imageResId", 0);
        String price = intent.getStringExtra("pricetag");


        // Log the price for debugging
        Log.d("PriceDebug", "Price passed: " + price);

        // Convert price to numeric type for calculations
        if (price != null && !price.isEmpty()) {
            try {
                itemPrice = Float.parseFloat(price.replace("Rs.", "").trim());  // Remove Rs. and parse
                foodPrice.setText("₹ " + itemPrice);
            } catch (NumberFormatException e) {
                itemPrice = 0.0f;
                foodPrice.setText("₹ 0.0");
            }
        }

        // Set data to UI components
        foodName.setText(name != null ? name : "N/A");
        foodIngredients.setText(ingredients != null ? ingredients : "N/A");
        foodImage.setImageResource(imageResId);
        foodPrice.setText("₹ " + itemPrice);

        // Increment quantity
        incrementButton.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
        });

        // Decrement quantity
        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantity();
            }
        });

        // Add to Cart functionality
        addToCartButton.setOnClickListener(v -> {
            String foodNameString = foodName.getText().toString().trim();
            String foodPriceString = foodPrice.getText().toString().trim();

            if (foodNameString.isEmpty()) {
                Toast.makeText(this, "Invalid food name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add to Cart via CartManager
            CartManager.getInstance().addToCart(foodNameString, quantity, itemPrice * quantity);

            // Show confirmation
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after adding to the cart
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateQuantity() {
        quantityTextView.setText(String.valueOf(quantity));
        foodPrice.setText("₹ " + (itemPrice * quantity));  // Update price when quantity changes
    }
}
