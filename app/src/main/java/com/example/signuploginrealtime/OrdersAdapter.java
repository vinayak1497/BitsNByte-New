package com.example.signuploginrealtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
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

        // Display order items
        StringBuilder itemsText = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsText.append(item.getName()).append(" - ₹")
                    .append(item.getPrice()).append(" x ")
                    .append(item.getQuantity()).append("\n");
        }
        holder.itemsTextView.setText(itemsText.toString().trim());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, studentNameTextView, totalAmountTextView, timestampTextView, itemsTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            studentNameTextView = itemView.findViewById(R.id.studentName);
            totalAmountTextView = itemView.findViewById(R.id.totalAmount);
            timestampTextView = itemView.findViewById(R.id.timestamp);
            itemsTextView = itemView.findViewById(R.id.items_list);
        }
    }
}
