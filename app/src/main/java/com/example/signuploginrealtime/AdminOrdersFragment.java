package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminOrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList;

    public AdminOrdersFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_orders_fragment, container, false); // Correct XML
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orderList, new OrdersAdapter.OnOrderStatusChangeListener() {
            @Override
            public void onAcceptOrder(Order order) {
                updateOrderStatus(order.getOrderId(), "Accepted", order.getUserId());

            }

            @Override
            public void onReadyOrder(Order order) {
                updateOrderStatus(order.getOrderId(), "Ready", order.getUserId());

            }
        });
        ordersRecyclerView.setAdapter(ordersAdapter);

        fetchOrdersFromDatabase();
        return view;
    }

    private void fetchOrdersFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        order.setOrderId(orderSnapshot.getKey()); // <<< Important line to add
                        orderList.add(order);
                    }
                }

                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOrderStatus(String orderId, String newStatus, String userId) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        DatabaseReference orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");

        // 1. Update status in Orders (admin panel)
        ordersRef.child(orderId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Order status updated successfully!", Toast.LENGTH_SHORT).show();

                    // 2. Update status in User's OrderHistory
                    if (userId != null) {
                        orderHistoryRef.child(userId).child(orderId).child("status").setValue(newStatus)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Optionally show a success message
                                    // Toast.makeText(getContext(), "User order history updated!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to update user order history: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update order status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
