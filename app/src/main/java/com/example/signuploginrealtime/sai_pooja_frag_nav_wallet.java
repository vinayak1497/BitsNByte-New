package com.example.signuploginrealtime;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class sai_pooja_frag_nav_wallet extends Fragment {

    TextView balance;
    Button addfunds;

    public sai_pooja_frag_nav_wallet() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_wallet, container, false);
        balance = view.findViewById(R.id.balance);
        addfunds = view.findViewById(R.id.addfunds);

        addfunds.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Addfundsgooglepay.class);
            startActivity(intent);

        });
        return view;
    }


}