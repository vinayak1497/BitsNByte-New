package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class sai_pooja_frag_nav_add_to_cart extends Fragment {

    TextView grandTotalTextView;
    RecyclerView cartRecyclerView;
    Button sendOrderButton, clearCartButton;

    public sai_pooja_frag_nav_add_to_cart() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_add_to_cart, container, false);
        grandTotalTextView = view.findViewById(R.id.Grand_total);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        sendOrderButton = view.findViewById(R.id.buy_now);
        clearCartButton = view.findViewById(R.id.clear_cart);

        displayCartItems();
        updateGrandTotal();

        sendOrderButton.setOnClickListener(v -> sendOrderToRealtimeDatabase());
        clearCartButton.setOnClickListener(v -> clearCartManually());

        return view;
    }

    private void displayCartItems() {
        ArrayList<CartItem> cartItems = CartManager.getInstance().getCartItems();

        CartItemAdapter cartItemAdapter = new CartItemAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartItemAdapter);
    }

    private void updateGrandTotal() {
        float grandTotal = CartManager.getInstance().getGrandTotal();
        grandTotalTextView.setText("₹ " + grandTotal);
    }

    private void sendOrderToRealtimeDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        ArrayList<CartItem> cartItems = CartManager.getInstance().getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Fetch user details from Singleton
        HelperClass user = UserDataSingleton.getInstance().getUserData();
        if (user == null || user.getUsername() == null || user.getName() == null) {
            Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique order ID
        String orderId = databaseReference.push().getKey();
        if (orderId == null) {
            Toast.makeText(getContext(), "Error generating order ID!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> orderData = new HashMap<>();
        ArrayList<Map<String, Object>> itemsList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.getName());
            itemMap.put("price", item.getTotalPrice());
            itemMap.put("quantity", item.getQuantity());
            itemsList.add(itemMap);
        }

        // Add user details to the order
        orderData.put("moodleId", user.getUsername()); // Example: "23102063"
        orderData.put("studentName", user.getName());  // Example: "Vinayak Umesh Kundar"
        orderData.put("items", itemsList);
        orderData.put("totalAmount", CartManager.getInstance().getGrandTotal());

        // Format timestamp for better readability
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));
        orderData.put("timestamp", formattedTimestamp);

        databaseReference.child(orderId).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                    CartManager.getInstance().clearCart(); // Clear cart after order placement
                    displayCartItems();
                    updateGrandTotal();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Order Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void clearCartManually() {
        CartManager.getInstance().clearCart();
        displayCartItems();
        updateGrandTotal();
        Toast.makeText(getContext(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
    }
}