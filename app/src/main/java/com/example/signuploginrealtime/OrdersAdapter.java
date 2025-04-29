package com.example.signuploginrealtime;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<Order> orders;
    private final OnOrderStatusChangeListener listener;

    public OrdersAdapter(List<Order> orders, OnOrderStatusChangeListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderIdTextView.setText("Order ID: " + order.getOrderId());
        holder.studentNameTextView.setText("Name: " + order.getStudentName());
        holder.totalAmountTextView.setText("Total: ₹" + order.getTotalAmount());
        holder.timestampTextView.setText("Time: " + order.getTimestamp());
        holder.statusTextView.setText("Status: " + order.getStatus());

        // Handle the button's text and color based on the order's status
        if ("Ongoing".equals(order.getStatus())) {
            holder.statusButton.setText("Accept");
            holder.statusButton.setBackgroundColor(Color.YELLOW); // Accept button (Yellow)
            holder.statusButton.setOnClickListener(v -> {
                listener.onAcceptOrder(order);
                holder.statusButton.setText("Ready");
                holder.statusButton.setBackgroundColor(Color.GREEN); // Ready button (Green)
            });
        } else if ("Accepted".equals(order.getStatus())) {
            holder.statusButton.setText("Ready");
            holder.statusButton.setBackgroundColor(Color.GREEN); // Ready button (Green)
            holder.statusButton.setOnClickListener(v -> {
                listener.onReadyOrder(order);
                holder.statusButton.setText("Order Ready");
                holder.statusButton.setBackgroundColor(Color.GRAY); // Finished (Grey)
            });
        } else {
            holder.statusButton.setText("Order Ready");
            holder.statusButton.setBackgroundColor(Color.GRAY); // Finished (Grey)
            holder.statusButton.setEnabled(false); // Disable the button once ready
        }

        // Display order items
        StringBuilder itemsText = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsText.append(item.getName())
                    .append(" - ₹").append(item.getPrice())
                    .append(" x ").append(item.getQuantity())
                    .append("\n");
        }
        holder.itemsTextView.setText(itemsText.toString().trim());
    }



    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, studentNameTextView, totalAmountTextView, timestampTextView, itemsTextView, statusTextView;
        Button statusButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            studentNameTextView = itemView.findViewById(R.id.studentName);
            totalAmountTextView = itemView.findViewById(R.id.totalAmount);
            timestampTextView = itemView.findViewById(R.id.timestamp);
            itemsTextView = itemView.findViewById(R.id.items_list);
            statusTextView = itemView.findViewById(R.id.status_text);
            statusButton = itemView.findViewById(R.id.status_button); // Add the button in your XML layout
        }
    }

    public interface OnOrderStatusChangeListener {
        void onAcceptOrder(Order order);  // For "Accept" click
        void onReadyOrder(Order order);   // For "Ready" click
    }

}
