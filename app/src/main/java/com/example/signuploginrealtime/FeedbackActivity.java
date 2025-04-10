package com.example.signuploginrealtime;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    EditText etSubject, etMessage;
    Button btnSubmitFeedback;

    DatabaseReference feedbackDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String username = getIntent().getStringExtra("username");

        setContentView(R.layout.activity_feedback);

        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);

        feedbackDbRef = FirebaseDatabase.getInstance().getReference("Feedbacks");

        btnSubmitFeedback.setOnClickListener(v -> {
            String subject = etSubject.getText().toString().trim();
            String message = etMessage.getText().toString().trim();

            if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(message)) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                saveFeedback(subject, message);
            }
        });
    }

    private void saveFeedback(String subject, String message) {
        String feedbackId = feedbackDbRef.push().getKey();
        String username = getIntent().getStringExtra("username"); // or get from session
        long timestamp = System.currentTimeMillis();

        Feedback feedback = new Feedback(feedbackId, subject, message, username, timestamp);

        feedbackDbRef.child(feedbackId).setValue(feedback)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Feedback submitted!", Toast.LENGTH_SHORT).show();
                    etSubject.setText("");
                    etMessage.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public static class Feedback {
        public String id, subject, message, username;
        public long timestamp;

        public Feedback() {
        }

        public Feedback(String id, String subject, String message, String username, long timestamp) {
            this.id = id;
            this.subject = subject;
            this.message = message;
            this.username = username;
            this.timestamp = timestamp;
        }
    }

}
