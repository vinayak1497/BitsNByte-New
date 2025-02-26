package com.example.signuploginrealtime;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class sai_pooja_frag_nav_home extends Fragment {
    private TextView userGreetingTextView;
    public sai_pooja_frag_nav_home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_home_page, container, false);
        userGreetingTextView = view.findViewById(R.id.user_greeting);
        // Retrieve the user's name from the intent extras
        String nameUser = getActivity().getIntent().getStringExtra("name");
        if (nameUser != null) {
            userGreetingTextView.setText("Hello, " + nameUser + "!");
        } else {
            userGreetingTextView.setText("Hello, User!");
        }
        return view;
    }
}