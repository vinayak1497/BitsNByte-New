package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class sai_pooja_frag_nav_profile extends Fragment {

    TextView profileName, profileEmail, profileUsername, profilePassword, profileDepartment;
    TextView titleName, titleUsername;
    Button editProfile;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_profile, container, false);

        // Initialize UI components
        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePassword = view.findViewById(R.id.profilePassword);
        profileDepartment = view.findViewById(R.id.profileDepartment1); // Check XML ID
        titleName = view.findViewById(R.id.titleName);
        titleUsername = view.findViewById(R.id.titleUsername);
        editProfile = view.findViewById(R.id.editButton);

        if (profileName == null || profileEmail == null || profileUsername == null || profilePassword == null || profileDepartment == null) {
            Toast.makeText(getActivity(), "Error: Some UI elements are missing!", Toast.LENGTH_LONG).show();
            return view;
        }

        // Fetch user data from Intent
        showUserData();

        editProfile.setOnClickListener(view1 -> passUserData());

        return view;
    }

    public void showUserData() {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            Toast.makeText(getActivity(), "Error: No user data found!", Toast.LENGTH_LONG).show();
            return;
        }

        String nameUser = intent.getStringExtra("name");
        String emailUser = intent.getStringExtra("email");
        String usernameUser = intent.getStringExtra("username");
        String passwordUser = intent.getStringExtra("password");
        String departmentUser = intent.getStringExtra("branch"); // Make sure this key matches the Intent data

        // Handle potential null values
        titleName.setText(nameUser != null ? nameUser : "N/A");
        titleUsername.setText(usernameUser != null ? usernameUser : "N/A");
        profileName.setText(nameUser != null ? nameUser : "N/A");
        profileEmail.setText(emailUser != null ? emailUser : "N/A");
        profileUsername.setText(usernameUser != null ? usernameUser : "N/A");
        profilePassword.setText(passwordUser != null ? passwordUser : "N/A");
        profileDepartment.setText(departmentUser != null ? departmentUser : "COMPS");
    }

    public void passUserData() {
        String userUsername = profileUsername.getText().toString().trim();

        if (userUsername.isEmpty()) {
            Toast.makeText(getActivity(), "Error: Username is empty!", Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String nameFromDB = userSnapshot.child("name").getValue(String.class);
                        String emailFromDB = userSnapshot.child("email").getValue(String.class);
                        String usernameFromDB = userSnapshot.child("username").getValue(String.class);
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        String departmentFromDB = userSnapshot.child("branch").getValue(String.class); // Ensure correct key

                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("branch", departmentFromDB);
                        startActivity(intent);
                        break;
                    }
                } else {
                    Toast.makeText(getActivity(), "User not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database Error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
