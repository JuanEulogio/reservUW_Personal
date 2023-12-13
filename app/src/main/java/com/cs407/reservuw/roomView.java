package com.cs407.reservuw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class roomView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        Button cancelButton = findViewById(R.id.cancelButton);
        Button reserveButton = findViewById(R.id.reserveButton);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView roomInfo = findViewById(R.id.boxedTextView);

        Intent receivedIntent = getIntent();
        String roomNum = receivedIntent.getStringExtra("roomNum");
        String buildingName = receivedIntent.getStringExtra("buildingName");

        String roomInfoText = "This room is located at " + buildingName + ".\n"
                + "Room Number: " + roomNum;

        roomInfo.setText(roomInfoText);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), reservationConfirmed.class);
                intent.putExtra("roomNum", roomNum);
                intent.putExtra("buildingName", buildingName);
                startActivity(intent);
                // modify/add the room to the reserved room array
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}