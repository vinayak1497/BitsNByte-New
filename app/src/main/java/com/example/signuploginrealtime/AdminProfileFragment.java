package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

public class AdminProfileFragment extends Fragment {

    private TextView earningText;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_profile_fragment, container, false);

        earningText = view.findViewById(R.id.earningText);

        calculateTotalEarnings();

        return view;
    }

    private void calculateTotalEarnings() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalEarning = 0.0;

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Object amountObj = orderSnapshot.child("totalAmount").getValue();
                    if (amountObj != null) {
                        try {
                            double orderAmount = Double.parseDouble(amountObj.toString());
                            totalEarning += orderAmount;
                        } catch (NumberFormatException e) {
                            e.printStackTrace(); // Optional: log for debugging
                        }
                    }
                }

                earningText.setText("₹ " + String.format("%.2f", totalEarning));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                earningText.setText("Error loading earnings.");
            }
        });
    }
}
