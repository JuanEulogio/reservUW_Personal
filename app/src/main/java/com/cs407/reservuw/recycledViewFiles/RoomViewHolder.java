package com.cs407.reservuw.recycledViewFiles;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs407.reservuw.R;

/**
 * The individual recycledView UI item that bridges the roomItem component to be used in recycledView
 * made in RoomAdaptor
 */
public class RoomViewHolder extends RecyclerView.ViewHolder {

    TextView roomNumView;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        roomNumView= itemView.findViewById(R.id.roomNum);
    }

    public void bind(Room_item Room_item) {
        roomNumView.setText(Room_item.getRoomNum());
    }
}
