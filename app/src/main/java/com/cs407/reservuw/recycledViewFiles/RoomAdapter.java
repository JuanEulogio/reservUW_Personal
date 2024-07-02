package com.cs407.reservuw.recycledViewFiles;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs407.reservuw.R;

import java.util.List;

/**
 * The recycledView maker, using Room_item and RoomViewHolder
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {

    Context context;
    List<Room_item> Room_items;
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Room_item Room_item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public RoomAdapter(Context context, List<Room_item> Room_items) {
        this.context = context;
        this.Room_items = Room_items;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomViewHolder(LayoutInflater.from(context).inflate(R.layout.room_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room_item currentRoomItem = Room_items.get(position);
        holder.bind(currentRoomItem);

        // Set click listener for the item view
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(currentRoomItem);
            }
        });

        //TODO: is this the place that i can just say "Room: " + ____ so only the ui name display
        // is effected?
        // Seems like the origin
        holder.roomNumView.setText(Room_items.get(position).getRoomNum());
    }

    @Override
    public int getItemCount() {
        return Room_items.size();
    }

}
