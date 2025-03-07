package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_canteen_selection extends AppCompatActivity {
    ImageView backButton;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_canteen_selection);
        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> finish());

        CardView cardview1 = findViewById(R.id.sai_pooja_canteen_cardview);
        cardview1.setOnClickListener(v -> {
            Intent intent = new Intent(admin_canteen_selection.this, Admin_check.class);
            startActivity(intent);
            finish();
        });

        CardView cardview2 = findViewById(R.id.nescafe_cardview);
        cardview2.setOnClickListener(v ->
                Toast.makeText(admin_canteen_selection.this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        );

        CardView cardview3 = findViewById(R.id.juice_centre_cardview);
        cardview3.setOnClickListener(v ->
                Toast.makeText(admin_canteen_selection.this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        );

        CardView cardview4 = findViewById(R.id.rancho_cardview);
        cardview4.setOnClickListener(v ->
                Toast.makeText(admin_canteen_selection.this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}