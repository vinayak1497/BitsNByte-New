package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;

public class Admin_sai_pooj_main_frag extends AppCompatActivity {

    private BottomNavigationView adminNavView;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_admin_sai_pooj_main_frag);

        adminNavView = findViewById(R.id.admin_nav_view);

        // Load default fragment (Orders)
        if (savedInstanceState == null) {
            loadFragment(new AdminOrdersFragment(), false);
        }

        // Handle Bottom Navigation Item Selection
        adminNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_orders) {
                    loadFragment(new AdminOrdersFragment(), false);
                } else if (id == R.id.navigation_menu) {
                    loadFragment(new AdminMenuFragment(), false);
                } else if (id == R.id.navigation_notifications) {
                    loadFragment(new AdminNotificationsFragment(), false);
                } else {
                    loadFragment(new AdminProfileFragment(), false);
                }
                return true;
            }
        });

        // Set default selected item (Orders)
        adminNavView.setSelectedItemId(R.id.navigation_orders);
    }

    public void loadFragment(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.admin_frag_container, fragment);
        ft.commit();
    }

    // Check if admin is logged in
    private boolean isAdminLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("adminPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isAdminLoggedIn", false);
    }
}
