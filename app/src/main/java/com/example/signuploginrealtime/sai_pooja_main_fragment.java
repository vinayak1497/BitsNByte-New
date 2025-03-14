package com.example.signuploginrealtime;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class sai_pooja_main_fragment extends AppCompatActivity {

    private BottomNavigationView bnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sai_pooja_main_fragment_nav);

        bnView = findViewById(R.id.trial_nav_vieww);

        if (savedInstanceState == null) {
            loadfrag(new sai_pooja_frag_nav_profile(), false);
        }

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    loadfrag(new sai_pooja_frag_nav_home(), false);
                } else if (id == R.id.navigation_wallet) {
                    loadfrag(new sai_pooja_frag_nav_wallet(), false);
                } else if (id == R.id.nav_search) {
                    loadfrag(new sai_pooja_frag_nav_search(), false);
                } else if (id == R.id.navigation_cart) {
                    loadfrag(new sai_pooja_frag_nav_add_to_cart(), false);
                } else {
                    loadfrag(new sai_pooja_frag_nav_profile(), false);  // Always use replace
                }
                return true; // Indicate that the selection was handled.
            }
        });

        // Set default selected item (Profile)
        bnView.setSelectedItemId(R.id.nav_profile);
    }

    public void loadfrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_container, fragment);  // Always replace, never add
        ft.commit();

    }

}
