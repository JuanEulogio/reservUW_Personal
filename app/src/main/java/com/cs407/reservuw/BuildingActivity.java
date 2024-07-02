package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs407.reservuw.recycledViewFiles.RoomAdapter;
import com.cs407.reservuw.recycledViewFiles.Room_item;
import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.uwRoomDatabase;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import android.widget.NumberPicker;
import android.widget.Toast;

public class BuildingActivity extends AppCompatActivity   {

    private PlacesClient placesClient;
    public String placeName;

    ImageView buildingImage;

    SharedPreferences sharedPreferences;
    uwRoomDatabase myDatabase;
    int uid;

    //for recycled view
    RecyclerView recyclerView;
    List<Room_item> roomItems = new ArrayList<>();
    RoomAdapter adapter;
    LinearLayoutManager LinearLayoutManager;


    List<Integer> reservedRooms;
    LiveData<List<Rooms>> roomQuery;


    //TODO: whats the point of it? Seems like its for filtering time reservation
    String selectedBuilding; //TODO: placeId and this are the same. Remove one
    int selectedHour;
    LocalDate selectedDate;


    //catches mainMenu building marker intent
    String placeId; //TODO: selectedBuilding and this are the same. Remove one. Probably this one

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //rendering activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_view);

        TextView buildingViewTitle = findViewById(R.id.building_view_title);

        //setting back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buildingImage= findViewById(R.id.buildingImageView);



        //recycleView initialize
        recyclerView = findViewById(R.id.buildingRecyclerView);
        adapter = new RoomAdapter(getApplicationContext(), roomItems);
        LinearLayoutManager= new LinearLayoutManager(this);



        //setting default selected building, date, and time filter
        Intent receivedIntent = getIntent();
        placeId = receivedIntent.getStringExtra("ID");

        selectedBuilding= placeId; //TODO: fix/change

        //by default, looks into reservable times in the next hour
        selectedHour = LocalTime.now().plusHours(1).getHour();
        selectedDate= LocalDateTime.now().plusHours(1).toLocalDate();




        //TODO: filter functionality button
        //setting filter button
        ImageButton filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = "filter ready to implement. Waiting on my creator to make it :)";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(getApplicationContext(), text, duration).show();
                //recycledViewCode(selectedBuilding, selectedDate.getMonthValue(), selectedDate.getDayOfMonth(), timePicker.getValue());

                //TODO: Activate modal for filter?
            }
        });

        //setting database
        myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();

        //TODO: uid is never needed here. delete?
        sharedPreferences = getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);
        uid = sharedPreferences.getInt ("uid", -1);



        //retrieving selectedBuilding image from placesAPI //TODO: this can be in its own function

        placesClient = Places.createClient(this); //sets the entry point to the Places API

        //first retrieves placesID from all possible reservable builidings
        Resources res = getResources();
        final String[] placesID = res.getStringArray(R.array.buildingPlacesID);

        //array that specify the fields placesAPI will GET
        List<List<Place.Field>> placeFields = new ArrayList<List<Place.Field>>(placesID.length);
        for(int i=0; i< placesID.length; i++){
            placeFields.add(Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.PHOTO_METADATAS));
        }

        //make a arr of requests objects, used to send to places API
        final FetchPlaceRequest[] requests = new FetchPlaceRequest[placesID.length];
        for(int i=0; i< placesID.length; i++){
            requests[i]= FetchPlaceRequest.newInstance(placesID[i], placeFields.get(i));
        }


        //Key= places ID
        //Content= building name
        HashMap<String, String> hashMapPlaceIDandName= new HashMap<String, String>(); //TODO: useless??

        //iterated through each placesID (request) till place = the selectedBuilding
        for(int i=0; i< placesID.length; i++){

            //sends fetch request
            placesClient.fetchPlace(requests[i]).addOnSuccessListener((response) -> {
                Place place = response.getPlace();

                //fetches selectedBuilding information when found
                if(place.getId().compareTo(placeId)==0){

                    // Get the photo metadata.
                    final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                    if (metadata == null || metadata.isEmpty()) {
                        Log.w(TAG, "No photo metadata.");
                        return;
                    }
                    final PhotoMetadata photoMetadata = metadata.get(0);

                    // Create a FetchPhotoRequest
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
                }

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();

                    //here Handle error with given status code.
                }
            });
        }


        /*
        * call recycledViewCode() during fetch becuase
        * can only get/store placeName via having recycled code inside our client request observe
        */
        //fetch request to get building name //TODO: this can be in its own function
        final List<Place.Field> placeFields2 = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields2);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            //TODO: looks unnecessary. place.getName() seems fine during recycledViewCode implementation
            placeName = place.getName();
            buildingViewTitle.setText(place.getName());

            //Call the method to execute the rest of the code, containing the recycled view
            //We implement recycled view here in order to use placeName that comes from our fetch respose
            recycledViewCode(placeName, selectedDate.getMonthValue(), selectedDate.getDayOfMonth(), selectedHour);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
                //Here we handle error with given status code.
            }
        });
    }



    private void recycledViewCode(String place,int month, int day, int hour) {
        //clean up for liveData to show updated list
        roomItems.clear();

        //TODO: looks unnecessary. parameter use seems fine
        placeName= place;

        //TODO: should we just init this variable here?
        reservedRooms= myDatabase.reservationDAO().getReservationByDayMonthHour(placeName, month, day, hour);


        //TODO: should we just init this variable here
        roomQuery = myDatabase.roomDAO().getRoomsByBuildingMonthDayHour(placeId, reservedRooms);


        //makes recycledView items (roomItems) out of our queries
        roomQuery.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    //TODO: I can change "Room: "+ room.roomNumber to just be room.roomNumbers?
                    roomItems.add(new Room_item("Room: "+ room.roomNumber, placeName, room.uid));
                }
            } else {
                Log.d(TAG, "reservations are null");
            }

            // Set up RecyclerView after fetching data
            recyclerView.setLayoutManager(LinearLayoutManager);
            recyclerView.setAdapter(adapter);

            //sets onClick for recycledView items
            adapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Room_item Room_item) {
                    Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                    intent.putExtra("roomNum", Room_item.getRoomNum());
                    intent.putExtra("buildingName", placeName);
                    intent.putExtra("roomUID", Room_item.getRoomUID());
                    //TODO: need year here?
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);

                    startActivity(intent);
                }
            });

        });
    }


}