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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        View view = inflater.inflate(R.layout.activity_admin_orders_fragment, container, false);
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(orderList);
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
                    String orderId = orderSnapshot.getKey();
                    String moodleId = orderSnapshot.child("moodleId").getValue(String.class);
                    String studentName = orderSnapshot.child("studentName").getValue(String.class);
                    double totalAmount = orderSnapshot.child("totalAmount").getValue(Double.class);
                    String timestamp = orderSnapshot.child("timestamp").getValue(String.class);

                    List<OrderItem> itemsList = new ArrayList<>();
                    DataSnapshot itemsSnapshot = orderSnapshot.child("items");
                    for (DataSnapshot item : itemsSnapshot.getChildren()) {
                        String name = item.child("name").getValue(String.class);
                        double price = item.child("price").getValue(Double.class);
                        int quantity = item.child("quantity").getValue(Integer.class);

                        itemsList.add(new OrderItem(name, price, quantity));
                    }

                    Order order = new Order(orderId, moodleId, studentName, itemsList, totalAmount, timestamp);
                    orderList.add(order);
                }
                ordersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load orders: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
