package com.example.signuploginrealtime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class sai_pooja_frag_nav_wallet extends Fragment implements PaymentResultListener {
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        handlePaymentSuccess(razorpayPaymentID);
    }

    @Override
    public void onPaymentError(int code, String response) {
        handlePaymentError(code, response);
    }
    private static final String TAG = "WalletFragment";
    private static final String PREFS_NAME = "WalletPrefs";
    private static final String BALANCE_KEY = "wallet_balance";

    TextView balance;
    Button addfunds;

    double currentBalance = 0.0;

    public sai_pooja_frag_nav_wallet() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Wallet fragment started");

        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_wallet, container, false);

        balance = view.findViewById(R.id.balance);
        addfunds = view.findViewById(R.id.addfunds);

        // Load balance from SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentBalance = Double.longBitsToDouble(prefs.getLong(BALANCE_KEY, Double.doubleToLongBits(0.0)));
        balance.setText("₹" + currentBalance);

        Checkout.preload(requireContext());
        Log.d(TAG, "Razorpay Checkout preloaded");

        addfunds.setOnClickListener(v -> {
            Log.d(TAG, "Add Funds button clicked");
            startPayment();
        });

        return view;
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("YOUR_API_KEY_HERE"); // Since, posting this on github, i had removed the api, you can paste it here your test api key and it would run perfectly!

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Test Wallet");
            options.put("description", "Adding ₹500 to Wallet");
            options.put("currency", "INR");
            options.put("amount", "50000"); // ₹500 = 50000 paise
            options.put("prefill.email", "testuser@example.com");
            options.put("prefill.contact", "9876543210");

            Log.d(TAG, "Starting Razorpay payment with options: " + options.toString());
            checkout.open(requireActivity(), options);

        } catch (Exception e) {
            Log.e("RazorpayError", "Payment start error", e);  // 🔥
            Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void handlePaymentSuccess(@NonNull String razorpayPaymentID) {
        Log.d(TAG, "Payment Success, ID: " + razorpayPaymentID);
        currentBalance += 500;
        balance.setText("₹" + currentBalance);
        Toast.makeText(requireContext(), "₹500 Added Successfully!", Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(BALANCE_KEY, Double.doubleToLongBits(currentBalance));
        editor.apply();
    }

    public void handlePaymentError(int code, @NonNull String response) {
        Log.e(TAG, "Payment failed - Code: " + code + ", Response: " + response);
        Toast.makeText(requireContext(), "Payment failed: " + response, Toast.LENGTH_SHORT).show();
    }

}
