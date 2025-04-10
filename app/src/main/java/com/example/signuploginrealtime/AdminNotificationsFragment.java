package com.example.signuploginrealtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminNotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private ArrayList<FeedbackActivity.Feedback> feedbackList;
    private DatabaseReference feedbackRef;

    public AdminNotificationsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin_notifications_fragment, container, false);
        recyclerView = view.findViewById(R.id.feedbackRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(feedbackList);
        recyclerView.setAdapter(adapter);

        feedbackRef = FirebaseDatabase.getInstance().getReference("Feedbacks");
        loadFeedbacks();

        return view;
    }

    private void loadFeedbacks() {
        feedbackRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FeedbackActivity.Feedback feedback = data.getValue(FeedbackActivity.Feedback.class);
                    if (feedback != null) {
                        feedbackList.add(feedback);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
