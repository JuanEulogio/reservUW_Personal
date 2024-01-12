package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.cs407.reservuw.recycledViewFiles.RoomAdapter;
import com.cs407.reservuw.recycledViewFiles.Room_item;
import com.cs407.reservuw.roomDB.FavoriteRoomDAO;
import com.cs407.reservuw.roomDB.Reservations;
import com.cs407.reservuw.roomDB.roomDAO;
import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.uwRoomDatabase;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private PlacesClient placesClient;


    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);


        //setting back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recycledViewCode();

    }




    private void recycledViewCode() {
        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();


        int uid = sharedPreferences.getInt ("uid", -1);

        FavoriteRoomDAO favoriteDAO = myDatabase.FavoriteRoomDAO();
        roomDAO myRoomDAO = myDatabase.roomDAO();


        List<Integer> favRoomId = favoriteDAO.getRoomsByUserID(uid);
        //test to see fav rooms for current user
        Log.i(TAG, favRoomId.toString().substring(1, favRoomId.toString().length()-1));


        LiveData<List<Rooms>> roomsByRoomId = myRoomDAO.getRoomsByRoomID(favRoomId);

        List<Room_item> Room_items = new ArrayList<>();
        roomsByRoomId.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber());
                    Room_items.add(new Room_item("Room: " + room.getRoomNumber(), room.getBuilding(), room.getUid()));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            // Set up RecyclerView after fetching data
            RecyclerView recyclerView = findViewById(R.id.favoritesRecyclerView);
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
                    intent.putExtra("roomUID", Room_item.getRoomUID());
                    LocalDateTime nowAfterOneHour= LocalDateTime.now().plusHours(1);
                    int hour= nowAfterOneHour.getHour();
                    int month= nowAfterOneHour.getMonthValue();
                    int day= nowAfterOneHour.plusHours(1).getDayOfMonth();

                    //we loop here and get the latest available reservation to reserv for that room
                    Log.i(TAG, "starting values. month: " + month + ", day:" + day + ", hour: " + hour);
                    Reservations reservations;
                    for(int i = hour; i<= 23; i++){
                        Log.i(TAG, "i: " + i);
                        reservations= myDatabase.reservationDAO().ifReservationExistAtTime(Room_item.getRoomUID(), month, day, i);
                        if(reservations== null){
                            hour= i;
                            break;
                        }
                        nowAfterOneHour= nowAfterOneHour.plusHours(1);
                        month= nowAfterOneHour.plusHours(1).getMonthValue();
                        day= nowAfterOneHour.getDayOfMonth();
                        Log.i(TAG, "month: " + month + ", day:" + day + ", hour: " + hour);
                        if(i==23){
                            Log.i(TAG, "going next day");
                            i= nowAfterOneHour.getHour()-1;
                        }

                    }
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);

                    //used to get the name of the building. Used because it will be used in the room view
                    //if the user clicks one of the rooms favorites
                    //Getting building name
                    placesClient = Places.createClient(getApplicationContext());

                    final String placeId = Room_item.getBuilding();
                    Log.i(TAG, "Building: " + Room_item.getBuilding());
                    final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                    final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                        Place place = response.getPlace();
                        String placeName= place.getName();
                        Log.i(TAG, "intent building name is: " + placeName);
                        intent.putExtra("buildingName", placeName);
                        //NOTE: dont move this. for some reason(i think it has to do with context)
                        // the intent wont save if we make one here, or if we save string info here
                        // to later use ourside this places add on success listener
                        startActivity(intent);

                        // Call the method to execute the rest of the code
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            final ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + exception.getMessage());
                            final int statusCode = apiException.getStatusCode();
                            // NOTE: Handle error with given status code.
                        }
                    });


                }
            });
        });
    }

}
