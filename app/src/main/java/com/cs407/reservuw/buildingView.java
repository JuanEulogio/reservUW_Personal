package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class buildingView extends AppCompatActivity {

    private PlacesClient placesClient;
    private String buildingName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_view);

        RecyclerView recyclerView = findViewById(R.id.buildingRecyclerView);

        TextView buildingViewTitle = findViewById(R.id.building_view_title);

        placesClient = Places.createClient(this);

        Intent receivedIntent = getIntent();
        final String placeId = receivedIntent.getStringExtra("ID");

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
            buildingViewTitle.setText(place.getName());
        }).addOnFailureListener((exception) ->{
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // NOTE: Handle error with given status code.
            }
        });

        List<item> items = new ArrayList<>();
        items.add(new item("2029","College Library",1));
        items.add(new item("2028","College Library",2));
        items.add(new item("2026","College Library",3));
        items.add(new item("2025","College Library",4));
        items.add(new item("3029","College Library",5));
        items.add(new item("3029","College Library",6));
        items.add(new item("4029","College Library",7));
        items.add(new item("5029","College Library",8));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BuildingAdapter(getApplicationContext(), items));

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}