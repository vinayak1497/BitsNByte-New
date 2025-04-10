package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpeningScreen extends AppCompatActivity {
    private Button button;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // Check if user already signed in and verified
        if (user != null && user.isEmailVerified()) {
            String userEmail = user.getEmail();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

            // Loop through database to match email and fetch user data
            ref.get().addOnSuccessListener(snapshot -> {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    HelperClass userData = snap.getValue(HelperClass.class);
                    if (userData != null && userData.getEmail().equals(userEmail)) {
                        UserDataSingleton.getInstance().setUserData(userData);

                        // Redirect to main fragment
                        Intent intent = new Intent(OpeningScreen.this, sai_pooja_main_fragment.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            });

            return; // Don't proceed to normal UI if already signed in
        }

        // If not signed in, show get started screen
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opening_page);

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(OpeningScreen.this, option_user_admin.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
