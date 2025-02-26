package com.example.signuploginrealtime;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDataSingleton {

    private static UserDataSingleton instance;
    private HelperClass userData;

    // Private constructor to prevent instantiation
    private UserDataSingleton() {
        userData = new HelperClass();
    }

    // Get the single instance of the class
    public static synchronized UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }

    // Set user data in the Singleton
    public void setUserData(HelperClass userData) {
        this.userData = userData;
    }

    // Get user data from the Singleton
    public HelperClass getUserData() {
        return userData;
    }

    // Retrieve user data from Firebase using Moodle ID (as the key)
    public void fetchUserData(String moodleId, DataFetchCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users")  // Reference to the "users" node
                .child(moodleId);  // Assuming Moodle ID is the key for the user

        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot snapshot = task.getResult();
                HelperClass user = snapshot.getValue(HelperClass.class);

                if (user != null) {
                    setUserData(user);  // Set the fetched user data into the Singleton
                    callback.onSuccess(user);  // Pass the user data to the callback
                } else {
                    callback.onFailure("User data not found.");
                }
            } else {
                callback.onFailure(task.getException() != null
                        ? task.getException().getMessage()
                        : "Failed to retrieve data.");
            }
        });
    }

    // Callback interface for asynchronous data retrieval
    public interface DataFetchCallback {
        void onSuccess(HelperClass user);  // Success callback
        void onFailure(String errorMessage);  // Failure callback
    }
}
