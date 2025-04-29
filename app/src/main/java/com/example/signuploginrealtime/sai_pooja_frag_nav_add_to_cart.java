package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    DatabaseReference orderHistoryRef;

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

        orderHistoryRef = FirebaseDatabase.getInstance().getReference("OrderHistory");

        displayCartItems();
        updateGrandTotal();

        sendOrderButton.setOnClickListener(v -> sendOrderToRealtimeDatabase());
        clearCartButton.setOnClickListener(v -> clearCartManually());
        TabLayout tabLayout = view.findViewById(R.id.orderTabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.orderViewPager);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) tab.setText("Ongoing");
                    else if (position == 1) tab.setText("Accepted");
                    else if (position == 2) tab.setText("Completed");
                }).attach();


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
        float grandTotal = CartManager.getInstance().getGrandTotal();

        // Step 1: Load wallet balance from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("WalletPrefs", Context.MODE_PRIVATE);
        double currentBalance = Double.longBitsToDouble(prefs.getLong("wallet_balance", Double.doubleToLongBits(0.0)));

        // Step 2: Check if user has sufficient balance
        if (grandTotal > currentBalance) {
            Toast.makeText(getContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        ArrayList<CartItem> cartItems = CartManager.getInstance().getCartItems();
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        HelperClass user = UserDataSingleton.getInstance().getUserData();
        if (user == null || user.getUsername() == null || user.getName() == null) {
            Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
            return;
        }

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

        orderData.put("moodleId", user.getUsername());
        orderData.put("studentName", user.getName());
        orderData.put("items", itemsList);
        orderData.put("totalAmount", grandTotal);
        orderData.put("status", "Ongoing");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String formattedTimestamp = sdf.format(new Date());
        orderData.put("timestamp", formattedTimestamp);

        // Step 3: Deduct wallet balance and save updated value
        double updatedBalance = currentBalance - grandTotal;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("wallet_balance", Double.doubleToLongBits(updatedBalance));
        editor.apply();

        // Step 4: Proceed with placing the order
        databaseReference.child(orderId).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
                    saveOrderHistory(user.getUsername(), orderId, orderData);
                    CartManager.getInstance().clearCart();
                    displayCartItems();
                    updateGrandTotal();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Order Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }



    private void saveOrderHistory(String userId, String orderId, Map<String, Object> orderData) {
        orderHistoryRef.child(userId).child(orderId).setValue(orderData);
    }

    private void clearCartManually() {
        CartManager.getInstance().clearCart();
        displayCartItems();
        updateGrandTotal();
        Toast.makeText(getContext(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
    }
}

