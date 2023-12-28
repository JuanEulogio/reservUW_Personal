package com.cs407.reservuw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class reservationConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmed);

        Intent receivedIntent = getIntent();
        String roomNum = receivedIntent.getStringExtra("roomNum");
        String buildingName = receivedIntent.getStringExtra("buildingName");
        TextView reservationLocation = findViewById(R.id.reservationLocation_textView);

        reservationLocation.setText(roomNum + ", " + buildingName);

        Button closeButton= findViewById(R.id.closeConfirmation_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(reservationConfirmedActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}