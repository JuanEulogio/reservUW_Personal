package com.cs407.reservuw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class reservationConfirmed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmed);

        Button closeButton= findViewById(R.id.closeConfirmation_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(reservationConfirmed.this, MainMenu.class);
                intent.putExtra("view", "main");
                startActivity(intent);
            }
        });
    }
}