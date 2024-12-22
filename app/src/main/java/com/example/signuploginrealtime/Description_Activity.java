package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Description_Activity extends AppCompatActivity {
    private ImageView food_image, back_button, cart_btn_image;
    private Button cart_btn;
    private TextView title, ingredients, shelf_life, price, plus, minus, number;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.description_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize views
        food_image = findViewById(R.id.item_image);
        cart_btn_image = findViewById(R.id.cart_image);
        cart_btn = findViewById(R.id.add_to_cart_btn);
        title = findViewById(R.id.title);
        ingredients = findViewById(R.id.ingredients_name);
        shelf_life = findViewById(R.id.shelf_life_time);
        price = findViewById(R.id.price_name);
        plus = findViewById(R.id.plus_btn);
        minus = findViewById(R.id.minus_btn);
        number = findViewById(R.id.value);

        // **Retrieve data from the intent**
        Intent intent = getIntent();
        String foodTitle = intent.getStringExtra("name");  // Get the title/name passed from RecyclerView
        int foodImageRes = intent.getIntExtra("imageResId", 0); // Get the image resource ID passed
        String ingredientstitle = intent.getStringExtra("ingredientsId");
        String pricetitle = intent.getStringExtra("pricetag");


        // **Set the data to the views**
        title.setText(foodTitle);  // Set the name/title to the title TextView
        food_image.setImageResource(foodImageRes);// Set the image to the ImageView// Set button text if needed (optional)
        ingredients.setText(ingredientstitle);
        price.setText(pricetitle);

        // **You can also fetch and display other data like ingredients, price, etc.**
        // For now, I am setting the basic data (name and image), you can add more as per your app logic.
    }
}
