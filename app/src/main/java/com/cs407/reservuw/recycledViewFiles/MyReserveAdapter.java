package com.cs407.reservuw.recycledViewFiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs407.reservuw.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MyReserveAdapter extends RecyclerView.Adapter<MyReserveViewHolder> {

    Context context;
    List<com.cs407.reservuw.recycledViewFiles.reservation_item> reservation_item;

    MyReserveAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(reservation_item reservation_item);
    }

    public void setOnItemClickListener(MyReserveAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyReserveAdapter(Context context, List<reservation_item> reservation_item) {
        this.context = context;
        this.reservation_item = reservation_item;
    }

    @NonNull
    @Override
    public MyReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyReserveViewHolder(LayoutInflater.from(context).inflate(R.layout.reservation_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyReserveViewHolder holder, int position) {
        reservation_item currentReservationItem = reservation_item.get(position);
        holder.bind(currentReservationItem);

        // Set click listener for the item view
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(currentReservationItem);
            }
        });


        holder.buildingTextView.setText(reservation_item.get(position).getBuilding());
        holder.roomNumTextView.setText(reservation_item.get(position).getRoomNum());

        //TODO: test this
        //took out ":ss" from pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Day: 'MM-dd '| Time: 'KK:00 a", Locale.ENGLISH);
        holder.timeDateTextView.setText(reservation_item.get(position).getTimeDate().format(formatter));
    }

    @Override
    public int getItemCount() {
        return reservation_item.size();
    }
}
