package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class sai_pooja_frag_nav_home extends Fragment {
    private TextView userGreetingTextView;
    private SearchView searchView;

    // Declare all ImageViews for home categories
    private ImageView MasalaDosa, Pizza, Pasta;
    private ImageView Burger, Frankie, PaneerChilli;
    private ImageView Samosa, Sandwich, TripleRice;
    private ImageView BurgerDiscount, PaneerTikkaDiscount, VadaPavDiscount;

    public sai_pooja_frag_nav_home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_home_page, container, false);
        setHasOptionsMenu(true);

        // Bind Views
        userGreetingTextView = view.findViewById(R.id.user_greeting);
        searchView = view.findViewById(R.id.searchView);

        MasalaDosa = view.findViewById(R.id.MD_masaladosa_image);
        Pizza = view.findViewById(R.id.MD_pizza_image);
        Pasta = view.findViewById(R.id.MD_pasta_image);

        Burger = view.findViewById(R.id.LD_burger_image);
        Frankie = view.findViewById(R.id.LD_frankie_image);
        PaneerChilli = view.findViewById(R.id.LD_paneer_chilli_image);

        Samosa = view.findViewById(R.id.YF_samosa_image);
        Sandwich = view.findViewById(R.id.YF_sandwhich_img);
        TripleRice = view.findViewById(R.id.YF_triple_rice_image);

        BurgerDiscount = view.findViewById(R.id.OD_Burger_image);
        PaneerTikkaDiscount = view.findViewById(R.id.OD_Paneer_image);
        VadaPavDiscount = view.findViewById(R.id.OD_VadaPav_image);

        Toolbar toolbar = view.findViewById(R.id.toolbar_home);
        if (toolbar != null) {
            ((sai_pooja_main_fragment) getActivity()).setSupportActionBar(toolbar);
        }

        // Set greeting
        String nameUser = UserDataSingleton.getInstance().getUserData().getName();
        userGreetingTextView.setText(nameUser != null ? "Hello, " + nameUser + "!" : "Hello, User!");

        // Search Logic
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

        // Click listeners for each food image
        Pizza.setOnClickListener(v -> startDescriptionActivity("Pizza", "Dough, tomato sauce, cheese, toppings", R.drawable.pizza, "150"));
        MasalaDosa.setOnClickListener(v -> startDescriptionActivity("Masala Dosa", "Rice crepe, Potato & onion filling", R.drawable.masaladosa_img, "50"));
        Pasta.setOnClickListener(v -> startDescriptionActivity("Pasta", "Pasta, sauce, vegetables", R.drawable.pasta, "120"));

        Burger.setOnClickListener(v -> startDescriptionActivity("Burger", "Beef/chicken patty, buns, toppings", R.drawable.burger_img, "80"));
        Frankie.setOnClickListener(v -> startDescriptionActivity("Frankie", "Tortilla, potatoes, onions, chutney", R.drawable.frankie_img, "30"));
        PaneerChilli.setOnClickListener(v -> startDescriptionActivity("Paneer Chilli", "Paneer cubes, peppers, spices", R.drawable.paneer_chilli_img, "100"));

        Samosa.setOnClickListener(v -> startDescriptionActivity("Samosa", "Potato filling, Crispy dough", R.drawable.samosa, "20"));
        Sandwich.setOnClickListener(v -> startDescriptionActivity("Sandwich", "Bread, fillings, vegetables", R.drawable.sandwhich, "40"));
        TripleRice.setOnClickListener(v -> startDescriptionActivity("Triple Rice", "Rice, curry, vegetables", R.drawable.triple_rice_img, "70"));

        BurgerDiscount.setOnClickListener(v -> startDescriptionActivity("Burger (Discount)", "Same taste, special price!", R.drawable.burger_img, "70"));
        PaneerTikkaDiscount.setOnClickListener(v -> startDescriptionActivity("Paneer Tikka (Discount)", "Paneer, tikka masala, yogurt", R.drawable.paneer_tikka_img, "110"));
        VadaPavDiscount.setOnClickListener(v -> startDescriptionActivity("Vada Pav (Discount)", "Potato patty, bun, chutneys", R.drawable.vadapav, "20"));

        return view;
    }

    private void navigateToSearchFragment(String query) {
        Bundle bundle = new Bundle();
        bundle.putString("searchQuery", query);

        sai_pooja_frag_nav_search searchFragment = new sai_pooja_frag_nav_search();
        searchFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_chat) {
            startActivity(new Intent(getActivity(), AiChatBotActivity.class));
            return true;
        } else if (id == R.id.menu_feedback) {
            startActivity(new Intent(getActivity(), FeedbackActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
