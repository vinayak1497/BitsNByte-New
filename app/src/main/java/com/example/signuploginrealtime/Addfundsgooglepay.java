package com.example.signuploginrealtime;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Addfundsgooglepay extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Checkout.preload(getApplicationContext());
        startPayment(); // directly open Razorpay
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("YOUR_API_KEY_HERE"); // Since, posting this on github, i had removed the api, you can paste it here your test api key and it would run perfectly!

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Bits and Byte Wallet");
            options.put("description", "Add Funds");
            options.put("currency", "INR");
            options.put("amount", "50000"); // ₹500 = 50000 paise

            JSONObject prefill = new JSONObject();
            prefill.put("email", "test@example.com");
            prefill.put("contact", "9876543210");

            options.put("prefill", prefill);

            checkout.open(Addfundsgooglepay.this, options);
        } catch (Exception e) {
            Log.e("RazorpayError", "Payment start error", e);  // 🔥 Add detailed log
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Funds Added: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        // 👇 You can go back to wallet or update balance
        finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Log.e("RazorpayError", "Payment failed - Code: " + code + ", Response: " + response);

        if (response != null && response.contains("payment_cancelled")) {
            Toast.makeText(this, "Payment Cancelled by you or UPI app timeout.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_LONG).show();
        }
        finish();
    }



}
