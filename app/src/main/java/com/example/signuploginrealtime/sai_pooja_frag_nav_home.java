package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class sai_pooja_frag_nav_home extends Fragment {
    private TextView userGreetingTextView;
    private SearchView searchView;

    public sai_pooja_frag_nav_home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sai_pooja_frag_nav_home_page, container, false);
        setHasOptionsMenu(true);

        // Set up Toolbar if defined in Fragment XML
        Toolbar toolbar = view.findViewById(R.id.toolbar_home);
        if (toolbar != null) {
            ((sai_pooja_main_fragment) getActivity()).setSupportActionBar(toolbar);
        }

        userGreetingTextView = view.findViewById(R.id.user_greeting);
        searchView = view.findViewById(R.id.searchView);

        String nameUser = UserDataSingleton.getInstance().getUserData().getName();
        if (nameUser != null) {
            userGreetingTextView.setText("Hello, " + nameUser);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu); // Your menu XML
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_chat) {
            // 🔹 Open AI ChatBot Activity
            Intent intent = new Intent(getActivity(), AiChatBotActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_feedback) {
            // 🔹 Open Feedback Activity
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_logout) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
