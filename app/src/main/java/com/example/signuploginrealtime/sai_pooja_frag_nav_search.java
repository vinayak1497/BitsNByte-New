package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class sai_pooja_frag_nav_search extends Fragment {

    private ArrayList<ContactModel> arrayContact = new ArrayList<>();
    private Recycler_Contact_Adapter adapter;

    public sai_pooja_frag_nav_search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sai_pooja_frag_nav_search_menu, container, false);

        SearchView searchView = rootView.findViewById(R.id.searchView);
        RecyclerView recyclerView = rootView.findViewById(R.id.RecyclerContact);

        // Set up RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Add dummy data to arrayContact
        setupContacts();

        // Initialize adapter and set it to RecyclerView
        adapter = new Recycler_Contact_Adapter(requireContext(), arrayContact);
        recyclerView.setAdapter(adapter);

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query); // Assuming filter method is implemented in the adapter
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // Live search filtering
                return true;
            }
        });



        return rootView;
    }

    public ArrayList<ContactModel> setupContacts() {
        arrayContact.add(new ContactModel(R.drawable.frankie_img, "Frankie", "add_to_cart",
                "Tortilla or paratha, Boiled potatoes, Chopped onions, Green chutney", "Rs. 30"));
        arrayContact.add(new ContactModel(R.drawable.burger_img, "Burger", "add_to_cart",
                "Burger buns, Patties (vegetable or meat), Lettuce, Tomato slices", "Rs. 30"));
        arrayContact.add(new ContactModel(R.drawable.masaladosa_img, "Masala Dosa", "add_to_cart",
                "Rice, Urad dal (split black gram), Potatoes, Ghee", "Rs. 40"));
        arrayContact.add(new ContactModel(R.drawable.triple_rice_img, "Triple Rice", "add_to_cart",
                "Basmati rice, Mixed vegetables, Soy sauce, Garlic", "Rs. 50"));
        arrayContact.add(new ContactModel(R.drawable.paneer_chilli_img, "Paneer Chilli", "add_to_cart",
                "Paneer (cottage cheese), Bell peppers, Onion, Soy sauce", "Rs. 50"));
        arrayContact.add(new ContactModel(R.drawable.paneer_tikka_img, "Paneer Tikka", "add_to_cart",
                "Paneer (cottage cheese), Yogurt, Red chili powder, Bell peppers", "Rs. 50"));
        arrayContact.add(new ContactModel(R.drawable.pizza_img, "Pizza", "add_to_cart",
                "Pizza base, Tomato sauce, Cheese (mozzarella), Toppings (bell peppers or pepperoni)", "Rs. 50"));
        arrayContact.add(new ContactModel(R.drawable.chinese_bhel, "Chinese Bhel", "add_to_cart",
                "Fried noodles, Mixed vegetables, Schezwan sauce, Soy sauce", "Rs. 25"));
        arrayContact.add(new ContactModel(R.drawable.pizza, "Pizza", "add_to_cart", "Dough, tomato sauce, cheese, toppings", "Rs. 80")); // Replace with actual details
        arrayContact.add(new ContactModel(R.drawable.pasta, "Pasta", "add_to_cart", "Pasta dough, sauce (tomato-based, cream-based, etc.), various ingredients (vegetables, meat, seafood)", "Rs. 60")); // Replace with actual details
        arrayContact.add(new ContactModel(R.drawable.samosa, "Samosa", "add_to_cart", "Dough, potato and pea filling, spices", "Rs. 15")); // Replace with actual details
        arrayContact.add(new ContactModel(R.drawable.sandwhich, "Sandwich", "add_to_cart", "Bread, fillings (vegetables, meat, cheese, spreads), condiments", "Rs. 15")); // Replace with actual details
        arrayContact.add(new ContactModel(R.drawable.vadapav, "Vada Pav", "add_to_cart", "Bun (pav), potato patty (vada), chutney", "Rs. 15")); // Replace with actual details
        return null;
    }
}
