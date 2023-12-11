package com.cs407.reservuw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyReserveAdapter extends RecyclerView.Adapter<MyReserveViewHolder> {

    Context context;
    List<item> items;

    public MyReserveAdapter(Context context, List<item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyReserveViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyReserveViewHolder holder, int position) {
        holder.roomNumView.setText(items.get(position).getRoomNum());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
