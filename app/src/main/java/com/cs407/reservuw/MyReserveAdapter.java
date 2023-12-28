package com.cs407.reservuw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs407.reservuw.recycledViewFiles.Room_item;

import java.util.List;

public class MyReserveAdapter extends RecyclerView.Adapter<MyReserveViewHolder> {

    Context context;
    List<Room_item> Room_items;

    public MyReserveAdapter(Context context, List<Room_item> Room_items) {
        this.context = context;
        this.Room_items = Room_items;
    }

    @NonNull
    @Override
    public MyReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyReserveViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyReserveViewHolder holder, int position) {
        holder.roomNumView.setText(Room_items.get(position).getRoomNum());
    }

    @Override
    public int getItemCount() {
        return Room_items.size();
    }
}
