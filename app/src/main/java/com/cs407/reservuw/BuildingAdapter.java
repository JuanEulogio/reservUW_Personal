package com.cs407.reservuw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingViewHolder> {

    Context context;
    List<item> items;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BuildingAdapter(Context context, List<item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BuildingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        item currentItem = items.get(position);
        holder.bind(currentItem);

        // Set click listener for the item view
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(currentItem);
            }
        });

        holder.roomNumView.setText(items.get(position).getRoomNum());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
