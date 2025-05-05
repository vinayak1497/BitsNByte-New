package com.example.signuploginrealtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private List<OrderHistoryItem> orderHistoryItems;
    private OnCancelClickListener onCancelClickListener;

    // Constructor
    public OrderHistoryAdapter(List<OrderHistoryItem> orderHistoryItems, OnCancelClickListener onCancelClickListener) {
        this.orderHistoryItems = orderHistoryItems;
        this.onCancelClickListener = onCancelClickListener;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout here (e.g., R.layout.order_history_item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistoryItem order = orderHistoryItems.get(position);
        // Bind data to your view holder
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderHistoryItems.size();
    }

    // ViewHolder class to hold the views
    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        // Define your views here
        TextView orderStatusTextView;
        Button cancelOrderButton;

        public OrderHistoryViewHolder(View itemView) {
            super(itemView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            cancelOrderButton = itemView.findViewById(R.id.cancelOrderButton);

            // Set click listener for the cancel button
            cancelOrderButton.setOnClickListener(v -> {
                // Get the current order at the adapter position
                OrderHistoryItem order = orderHistoryItems.get(getAdapterPosition());
                onCancelClickListener.onCancelClick(order); // Notify the listener
            });
        }

        // Bind data to the views
        public void bind(OrderHistoryItem order) {
            orderStatusTextView.setText(order.getStatus());
        }
    }

    // Interface for the cancel click listener
    public interface OnCancelClickListener {
        void onCancelClick(OrderHistoryItem order);
    }
}
