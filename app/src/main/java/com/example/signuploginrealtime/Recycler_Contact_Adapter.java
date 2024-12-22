package com.example.signuploginrealtime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_Contact_Adapter extends RecyclerView.Adapter<Recycler_Contact_Adapter.ViewHolder> {
    private final Context context;
    private final ArrayList<ContactModel> arrayContact; // Original dataset
    private ArrayList<ContactModel> filteredList; // Filtered dataset

    public Recycler_Contact_Adapter(Context context, ArrayList<ContactModel> arrayContact) {
        this.context = context;
        this.arrayContact = arrayContact;
        this.filteredList = new ArrayList<>(arrayContact); // Initialize filtered list with the original dataset
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactModel currentItem = filteredList.get(position);
        holder.imgview.setImageResource(currentItem.getImg());
        holder.txtname.setText(currentItem.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Description_Activity.class);
            intent.putExtra("name", currentItem.getName());
            intent.putExtra("imageResId", currentItem.getImg());
            intent.putExtra("ingredientsId", currentItem.getIngredients());
            intent.putExtra("pricetag", currentItem.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size(); // Return the size of the filtered list
    }

    // Filter method to filter the dataset based on the query
    public void filter(String query) {
        if (query.isEmpty()) {
            // If the search query is empty, restore the original dataset
            filteredList.clear();
            filteredList.addAll(arrayContact);
        } else {
            // Filter the original dataset based on the query
            String filterPattern = query.toLowerCase().trim();
            ArrayList<ContactModel> filtered = new ArrayList<>();
            for (ContactModel item : arrayContact) {
                if (item.getName().toLowerCase().contains(filterPattern)) {
                    filtered.add(item);
                }
            }
            filteredList.clear();  // Clear the existing filteredList
            filteredList.addAll(filtered); // Add all filtered items
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtname;
        final Button butn;
        final ImageView imgview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.textView);
            butn = itemView.findViewById(R.id.button);
            imgview = itemView.findViewById(R.id.imageView);
        }
    }
}
