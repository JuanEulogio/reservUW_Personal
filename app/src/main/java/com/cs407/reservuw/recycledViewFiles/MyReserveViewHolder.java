package com.cs407.reservuw.recycledViewFiles;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs407.reservuw.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MyReserveViewHolder extends RecyclerView.ViewHolder {

    TextView buildingTextView;
    TextView roomNumTextView;
    TextView timeDateTextView;

    public MyReserveViewHolder(@NonNull View itemView) {
        super(itemView);
        buildingTextView = itemView.findViewById(R.id.reservBuilding);
        roomNumTextView = itemView.findViewById(R.id.reservRoomNum);
        timeDateTextView = itemView.findViewById(R.id.reservTimeDate);
    }

    public void bind(reservation_item reservation_item) {
        buildingTextView.setText(reservation_item.getBuilding());

        roomNumTextView.setText(reservation_item.getRoomNum());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Day: 'MM-dd '| Time: 'HH:00 a", Locale.ENGLISH);
        String now = LocalDateTime.now().format(formatter);

        timeDateTextView.setText(now);

    }

}
