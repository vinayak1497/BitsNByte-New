package com.example.signuploginrealtime;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText signupName, signupEmail, signupMoodle, signupPassword, signupContactNo;
    private TextView loginRedirectText;
    Spinner branchSpinner, DivSpinner, YearSpinner;
    private Button signupButton;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupMoodle = findViewById(R.id.signup_moodle);
        signupContactNo = findViewById(R.id.signup_contact_no);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        branchSpinner = findViewById(R.id.signup_branch_dropdown);
        DivSpinner = findViewById(R.id.signup_division_dropdown);
        YearSpinner = findViewById(R.id.signup_year_dropdown);

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Signup button click listener
        signupButton.setOnClickListener(view -> handleSignup());

        // Redirect to LoginActivity
        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        String[] Division = {"A", "B", "C", "D", "E", "F"};
        String[] Year = {"F.E", "S.E", "T.E", "B.E"};
        String[] branches = {"Comps", "IT", "AIML", "DS", "Mech", "Civil", "ECE"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, branches);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Division);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Year);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapter1);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DivSpinner.setAdapter(adapter2);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        YearSpinner.setAdapter(adapter3);


        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBranch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        DivSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBranch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        YearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBranch = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void handleSignup() {
        // Get input from user
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupMoodle.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String contactNo = signupContactNo.getText().toString().trim();
        String branch = branchSpinner.getSelectedItem().toString();
        String division = DivSpinner.getSelectedItem().toString();
        String year = YearSpinner.getSelectedItem().toString();


        // Validate input
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user object
        HelperClass helperClass = new HelperClass(name, email, username, password, contactNo, branch, division, year);

        // Save to Firebase
        reference.child(username).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                Log.d("SignupActivity", "Data written to Firebase successfully");
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Prevent going back to signup
            } else {
                Toast.makeText(SignupActivity.this, "Signup failed. Try again later.", Toast.LENGTH_SHORT).show();
                Log.e("SignupActivity", "Error writing to Firebase", task.getException());
            }
        });
    }
}
