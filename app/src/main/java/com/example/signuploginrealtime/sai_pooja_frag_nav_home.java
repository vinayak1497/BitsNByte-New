package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class sai_pooja_frag_nav_home extends Fragment {
    private TextView userGreetingTextView;
    private SearchView searchView;
    private ImageView pizzaImage;
    private ImageView masalaDosaImage;
    private ImageView pastaImage;
    // Add more ImageViews for other food items

    public sai_pooja_frag_nav_home() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_home_page, container, false);

        userGreetingTextView = view.findViewById(R.id.user_greeting);
        searchView = view.findViewById(R.id.searchView);

        pizzaImage = view.findViewById(R.id.pizza_image);
        masalaDosaImage = view.findViewById(R.id.masaladosa_image);
        pastaImage = view.findViewById(R.id.pasta_image);
        // Initialize other food ImageViews here

        String nameUser = UserDataSingleton.getInstance().getUserData().getName();
        if (nameUser != null) {
            userGreetingTextView.setText("Hello, " + nameUser + "!");
        } else {
            userGreetingTextView.setText("Hello, User!");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navigateToSearchFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        pizzaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDescriptionActivity(
                        "Pizza",
                        "Dough, tomato sauce, cheese, toppings", // Main ingredients
                        R.drawable.pizza,
                        "250" // Replace with actual price
                );
            }
        });

        masalaDosaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDescriptionActivity(
                        "Masala Dosa",
                        "Rice, Urad dal (split black gram), Potatoes, Ghee",
                        R.drawable.masaladosa_img,
                        "40"
                );
            }
        });

        pastaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDescriptionActivity(
                        "Pasta",
                        "Pasta dough, sauce, various ingredients", // Main ingredients
                        R.drawable.pasta,
                        "Price" // Replace with actual price
                );
            }
        });
        // Set onClickListeners for other food ImageViews similarly

        return view;
    }

    private void navigateToSearchFragment(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", query);

        sai_pooja_frag_nav_search searchFragment = new sai_pooja_frag_nav_search();
        searchFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.frag_container, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startDescriptionActivity(String foodName, String ingredients, int imageResId, String price) {
        Intent intent = new Intent(getActivity(), Description_Activity.class);
        intent.putExtra("name", foodName);
        intent.putExtra("ingredientsId", ingredients);
        intent.putExtra("imageResId", imageResId);
        intent.putExtra("pricetag", price);
        startActivity(intent);
    }
}
