package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.roomDAO;
import com.cs407.reservuw.roomDB.uwRoomDatabase;
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
    private String placeName;

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
            placeName = place.getName();
            buildingViewTitle.setText(place.getName());

            // Call the method to execute the rest of the code
            executeRemainingCode(placeId);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // NOTE: Handle error with given status code.
            }
        });
    }

    private void executeRemainingCode(String placeId) {
        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .build();

        roomDAO myRoomDAO = myDatabase.roomDAO();
        LiveData<List<Rooms>> roomsByBuilding = myRoomDAO.getRoomsByBuilding(placeId);

        List<item> items = new ArrayList<>();

        roomsByBuilding.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber());
                    items.add(new item(Integer.toString(room.getRoomNumber()), placeName));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            Log.i(TAG, placeName);

            // Set up RecyclerView after fetching data
            RecyclerView recyclerView = findViewById(R.id.buildingRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            BuildingAdapter adapter = new BuildingAdapter(getApplicationContext(), items);
            recyclerView.setAdapter(adapter);


            ImageButton backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });



            adapter.setOnItemClickListener(new BuildingAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(item item) {
                    // Handle item click here
                    // Example: You can open a new activity or perform any action
                    // based on the clicked item.
                    // Access item details like item.getRoomNumber(), item.getPlaceName(), etc.
                    Intent intent = new Intent(getApplicationContext(), roomView.class);
                    intent.putExtra("roomNum", item.getRoomNum());
                    intent.putExtra("buildingName", item.getBuilding());
                    startActivity(intent);
                }
            });
        });


    }
}
