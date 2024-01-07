package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs407.reservuw.recycledViewFiles.RoomAdapter;
import com.cs407.reservuw.recycledViewFiles.Room_item;
import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.roomDAO;
import com.cs407.reservuw.roomDB.uwRoomDatabase;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuildingActivity extends AppCompatActivity {

    private PlacesClient placesClient;
    private String placeName;

    ImageView buildingImage;


    LocalDateTime reserveTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_view);


        TextView buildingViewTitle = findViewById(R.id.building_view_title);

        buildingImage= findViewById(R.id.buildingImageView);



        //setting back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Getting building name
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

            // Call the method to execute the rest of the code, containing the recycled view
            executeRemainingCode(placeId);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                // NOTE: Handle error with given status code.
            }
        });


        //Getting building image
        Log.i(TAG, "Place ID intent received: " + placeId);
        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            final Place place = response.getPlace();

            // Get the photo metadata.
            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
            if (metadata == null || metadata.isEmpty()) {
                Log.w(TAG, "No photo metadata.");
                return;
            }
            final PhotoMetadata photoMetadata = metadata.get(0);

            // Get the attribution text.
                //final String attributions = photoMetadata.getAttributions();

            // Create a FetchPhotoRequest.
            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                buildingImage.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();

                        //Handle error with given status code.
                }
            });
        });



        //TODO: setting reservation button/clock


        //TODO: set button to change building(we will change activity for this)



    }


    private void executeRemainingCode(String placeId) {
        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .build();



        //get only by a specific time available
        roomDAO myRoomDAO = myDatabase.roomDAO();
        LiveData<List<Rooms>> roomsByBuilding = myRoomDAO.getRoomsByBuilding(placeId);

        List<Room_item> Room_items = new ArrayList<>();

        roomsByBuilding.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber() + "ROOM UID" + room.getUid());
                    Room_items.add(new Room_item("Room " + Integer.toString(room.getRoomNumber()), placeName, room.getUid()));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            Log.i(TAG, placeName);

            // Set up RecyclerView after fetching data
            RecyclerView recyclerView = findViewById(R.id.buildingRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            RoomAdapter adapter = new RoomAdapter(getApplicationContext(), Room_items);
            recyclerView.setAdapter(adapter);




            adapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Room_item Room_item) {
                    // Handle item click here
                    // Example: You can open a new activity or perform any action
                    // based on the clicked item.
                    // Access item details like item.getRoomNumber(), item.getPlaceName(), etc.
                    Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                    intent.putExtra("roomNum", Room_item.getRoomNum());
                    intent.putExtra("buildingName", Room_item.getBuilding());
                    intent.putExtra("roomUID", Room_item.getRoomUID());
                    //TODO: send the localTimeDate for whatever is in the setted timeDate
                    startActivity(intent);
                }
            });
        });


    }
}