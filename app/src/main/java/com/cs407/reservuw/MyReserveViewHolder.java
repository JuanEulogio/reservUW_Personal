package com.cs407.reservuw;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyReserveViewHolder extends RecyclerView.ViewHolder {

    TextView roomNumView;

    public MyReserveViewHolder(@NonNull View itemView) {
        super(itemView);
        roomNumView = itemView.findViewById(R.id.roomNum);
    }

}
