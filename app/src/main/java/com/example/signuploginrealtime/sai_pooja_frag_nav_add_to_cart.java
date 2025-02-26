package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class sai_pooja_frag_nav_add_to_cart extends Fragment {

    TextView grandTotalTextView;
    RecyclerView cartRecyclerView;

    public sai_pooja_frag_nav_add_to_cart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_add_to_cart, container, false);
        grandTotalTextView = view.findViewById(R.id.Grand_total);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);  // Assuming you have a RecyclerView in your layout

        // Display cart items and update the grand total
        displayCartItems();
        updateGrandTotal();

        return view;
    }

    private void displayCartItems() {
        ArrayList<CartItem> cartItems = CartManager.getInstance().getCartItems();

        // Create and set the RecyclerView adapter
        CartItemAdapter cartItemAdapter = new CartItemAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartItemAdapter);
    }

    private void updateGrandTotal() {
        float grandTotal = CartManager.getInstance().getGrandTotal();
        grandTotalTextView.setText("₹ " + grandTotal);
    }
}
