package com.example.signuploginrealtime;

import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText signupName, signupEmail, signupMoodle, signupPassword, signupContactNo;
    private TextView loginRedirectText;
    Spinner branchSpinner, DivSpinner, YearSpinner;
    private Button signupButton;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

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
        backBtn = findViewById(R.id.back_btn);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, option_user_admin.class);
            startActivity(intent);
        });

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Signup button click listener

        signupButton.setOnClickListener(view -> {
            if (validateInputFields()) {
                registerUser();
            }
        });

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

        signupContactNo.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10)});

    }

    private void handleSignup() {
        // Get input from user
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String moodleId = signupMoodle.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String contactNo = signupContactNo.getText().toString().trim();
        String branch = branchSpinner.getSelectedItem().toString();
        String division = DivSpinner.getSelectedItem().toString();
        String year = YearSpinner.getSelectedItem().toString();

        if (!validateInputFields()) {
            return; // If input is not valid, stop here
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // Create Firebase Authentication account
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Send verification email
                        mAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(verifyTask -> {
                                    if (verifyTask.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Verification email sent to " + email, Toast.LENGTH_LONG).show();

                                        // Save to Firebase Realtime Database
                                        HelperClass helperClass = new HelperClass(name, email, moodleId, password, contactNo, branch, division, year);

                                        reference.child(moodleId).setValue(helperClass).addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Log.d("SignupActivity", "Data saved in database");

                                                // Save to Singleton
                                                UserDataSingleton.getInstance().setUserData(helperClass);

                                                // Redirect to login
                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                intent.putExtra("emailVerification", true);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SignupActivity.this, "Failed to save data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(SignupActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInputFields() {
        boolean isValid = true;

        if (signupName.getText().toString().trim().isEmpty()) {
            signupName.setError("Name is required");
            isValid = false;
        }

        if (signupMoodle.getText().toString().trim().isEmpty()) {
            signupMoodle.setError("Moodle ID is required");
            isValid = false;
        }

        if (signupEmail.getText().toString().trim().isEmpty()) {
            signupEmail.setError("Email is required");
            isValid = false;
        }

        if (signupPassword.getText().toString().trim().isEmpty()) {
            signupPassword.setError("Password is required");
            isValid = false;
        }

        if (signupContactNo.getText().toString().trim().isEmpty()) {
            signupContactNo.setError("Contact number is required");
            isValid = false;
        }

        return isValid;
    }

    private void registerUser() {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String username = signupMoodle.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(verifyTask -> {
                                        if (verifyTask.isSuccessful()) {

                                            // Store user data in Realtime DB
                                            storeUserDataInFirebase();

                                            // Redirect to verify email page
                                            Intent intent = new Intent(SignupActivity.this, VerifyEmailActivity.class);
                                            intent.putExtra("username", username);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    } else {
                        Toast.makeText(this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void storeUserDataInFirebase() {
        String name = signupName.getText().toString().trim();
        String username = signupMoodle.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String contact = signupContactNo.getText().toString().trim();
        String branch = branchSpinner.getSelectedItem().toString();
        String division = DivSpinner.getSelectedItem().toString();
        String year = YearSpinner.getSelectedItem().toString();

        HelperClass helperClass = new HelperClass(name, email, username, password, contact, branch, division, year);
        FirebaseDatabase.getInstance().getReference("users")
                .child(username)
                .setValue(helperClass);
    }
}
