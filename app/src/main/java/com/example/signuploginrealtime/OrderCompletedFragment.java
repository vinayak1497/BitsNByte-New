package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderCompletedFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference orderRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCompleted);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadOrders();

        return view;
    }

    private void loadOrders() {
        HelperClass user = UserDataSingleton.getInstance().getUserData();
        if (user == null || user.getUsername() == null) {
            return;
        }

        orderRef = FirebaseDatabase.getInstance().getReference("OrderHistory").child(user.getUsername());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<OrderHistoryItem> list = new ArrayList<>();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderHistoryItem order = orderSnapshot.getValue(OrderHistoryItem.class);
                    if (order != null && "Completed".equalsIgnoreCase(order.getStatus())) {
                        list.add(order);
                    }
                }

                // Pass the list and the OnCancelClickListener to the adapter
                OrderHistoryAdapter adapter = new OrderHistoryAdapter(list, new OrderHistoryAdapter.OnCancelClickListener() {
                    @Override
                    public void onCancelClick(OrderHistoryItem order) {
                        DatabaseReference singleOrderRef = FirebaseDatabase.getInstance().getReference("OrderHistory")
                                .child(user.getUsername())
                                .child(order.getOrderId());

                        singleOrderRef.child("status").setValue("Cancelled")
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(getContext(), "Order cancelled", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Failed to cancel order", Toast.LENGTH_SHORT).show());
                    }
                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load completed orders", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
