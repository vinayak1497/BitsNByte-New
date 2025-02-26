package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AdminMenuFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etFoodName, etIngredients, etPrice, etCategory;
    private ImageView ivFoodImage;
    private Button btnAddFood, btnChooseImage;
    private Uri imageUri;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private String encodedImage; // Stores Base64 image

    public AdminMenuFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_menu_fragment, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etFoodName = view.findViewById(R.id.etFoodName);
        ivFoodImage = view.findViewById(R.id.ivFoodImage);
        etIngredients = view.findViewById(R.id.etIngredients);
        etPrice = view.findViewById(R.id.etPrice);
        etCategory = view.findViewById(R.id.etCategory);
        btnAddFood = view.findViewById(R.id.btnAddFood);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Adding Food...");

        // Choose Image Button Click Listener
        btnChooseImage.setOnClickListener(v -> chooseImage());

        // Button Click Listener for Adding Food
        btnAddFood.setOnClickListener(v -> addFoodToFirestore());

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivFoodImage.setImageURI(imageUri);

            // Convert image to Base64
            encodeImageToBase64(imageUri);
        }
    }

    private void encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to encode image", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFoodToFirestore() {
        String foodName = etFoodName.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (foodName.isEmpty() || ingredients.isEmpty() || priceStr.isEmpty() || category.isEmpty() || encodedImage == null) {
            Toast.makeText(getContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Firestore Data
        Map<String, Object> foodItem = new HashMap<>();
        foodItem.put("name", foodName);
        foodItem.put("image", encodedImage); // Save Base64 Image
        foodItem.put("ingredients", ingredients);
        foodItem.put("price", price);
        foodItem.put("category", category);
        foodItem.put("availability", true);
        foodItem.put("timestamp", FieldValue.serverTimestamp());

        db.collection("food").add(foodItem)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Food Added Successfully!", Toast.LENGTH_SHORT).show();
                    etFoodName.setText("");
                    ivFoodImage.setImageResource(0);
                    etIngredients.setText("");
                    etPrice.setText("");
                    etCategory.setText("");
                    encodedImage = null;
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.e("Firestore", "Error adding document", e);
                    Toast.makeText(getContext(), "Error adding food: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
