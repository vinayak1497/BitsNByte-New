package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Admin_check extends AppCompatActivity {
    private static final String ADMIN_PASSWORD = "bits123"; // Change this to your actual password
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check);

        passwordInput = findViewById(R.id.signup_password);
        loginButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordInput.getText().toString().trim();

                if (enteredPassword.equals(ADMIN_PASSWORD)) {
                    // Authentication successful, go to next activity
                    Intent intent = new Intent(Admin_check.this, Admin_sai_pooj_main_frag.class); // Change NextActivity.class to your target activity
                    startActivity(intent);
                    finish();
                } else {
                    // Incorrect password, show error message
                    passwordInput.setError("Incorrect password");
                    Toast.makeText(Admin_check.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
