package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView verifyMsg;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();
        verifyMsg = findViewById(R.id.verifyMessage);
        FirebaseUser user = mAuth.getCurrentUser();
        username = getIntent().getStringExtra("username");

        if (user != null) {
            verifyMsg.setText("Verification email sent to: " + user.getEmail());
        }

        // Check every 3 seconds if email is verified
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user.reload().addOnSuccessListener(unused -> {
                    if (user.isEmailVerified()) {
                        proceedToLogin(user);
                    } else {
                        new Handler().postDelayed(this, 3000);
                    }
                });
            }
        }, 3000);
    }

    private void proceedToLogin(FirebaseUser firebaseUser) {
        // Fetch data from Realtime DB using email or username (your choice)
        String email = firebaseUser.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.child(username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                HelperClass user = task.getResult().getValue(HelperClass.class);
                UserDataSingleton.getInstance().setUserData(user);
                Intent intent = new Intent(VerifyEmailActivity.this, sai_pooja_main_fragment.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            }
        });

        ref.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot snap : snapshot.getChildren()) {
                Log.d("FirebaseData", snap.getKey() + ": " + snap.child("email").getValue());
            }
        });

    }


}
