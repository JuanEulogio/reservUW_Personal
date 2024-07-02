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


        //setting the recycled view list
        recycledViewCode();
    }




    private void recycledViewCode() {
        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();


        int uid = sharedPreferences.getInt ("uid", -1);


        //getting users favorite rooms. First by querying those rooms ids,
        // then using the ids to query the room object
        FavoriteRoomDAO favoriteDAO = myDatabase.FavoriteRoomDAO();
        List<Integer> favRoomId = favoriteDAO.getRoomsByUserID(uid);

        roomDAO myRoomDAO = myDatabase.roomDAO();
        LiveData<List<Rooms>> roomsByRoomId = myRoomDAO.getRoomsByRoomID(favRoomId);

        //setting the recycled views items
        List<Room_item> Room_items = new ArrayList<>();
        roomsByRoomId.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    //TODO: try to see if you can fix that 'Room: ' issue
                    Room_items.add(new Room_item("Room: " + room.getRoomNumber(), room.getBuilding(), room.getUid()));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            //TODO: in BuildingActivity this is a global var and initualized in onCrete.The only thing
            // here is:
            //            recyclerView.setLayoutManager(LinearLayoutManager);
            //            recyclerView.setAdapter(adapter);
            // Set up RecyclerView after fetching data
            RecyclerView recyclerView = findViewById(R.id.favoritesRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            RoomAdapter adapter = new RoomAdapter(getApplicationContext(), Room_items);
            recyclerView.setAdapter(adapter);


            adapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Room_item Room_item) {
                    //takes you to RoomActivity to prepares a reservation at the latest
                    //available reservable time
                    Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                    intent.putExtra("roomNum", Room_item.getRoomNum());
                    intent.putExtra("roomUID", Room_item.getRoomUID());

                    //
                    //TODO: make function that returns the month day and hour we want
                    LocalDateTime oneHourAfterNow= LocalDateTime.now().plusHours(1);
                    int hour= oneHourAfterNow.getHour();
                    int month= oneHourAfterNow.getMonthValue();
                    int day= oneHourAfterNow.plusHours(1).getDayOfMonth();

                    //loop to find the latest available reservation time
                    Reservations reservations;
                    for(int i = hour; i<= 23; i++){
                        reservations= myDatabase.reservationDAO().ifReservationExistAtTime(Room_item.getRoomUID(), month, day, i);

                        if(reservations== null){
                            //stop when we find a empty time slot
                            hour= i;
                            break;
                        }

                        //goes to the next hour
                        oneHourAfterNow= oneHourAfterNow.plusHours(1);
                        month= oneHourAfterNow.plusHours(1).getMonthValue();
                        day= oneHourAfterNow.getDayOfMonth();
                        //TODO: ???? whats this
                        if(i==23){
                            //TODO: "Log.i(TAG, "going next day");"???
                            // find the reason why i coded this and mention it here
                            i= oneHourAfterNow.getHour()-1;
                        }

                    }
                    //

                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);


                    /*
                     used to get the name of the building. Needed because it will be used in the roomActivity
                     if the user clicks one of the rooms favorites
                     */
                    placesClient = Places.createClient(getApplicationContext());

                    //rooms are tied to building using the buildings placeID, placed by placesAPI
                    //we use this placeId to fetch the places name
                    final String placeId = Room_item.getBuilding();
                    final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                    final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                        Place place = response.getPlace();
                        String placeName= place.getName();

                        intent.putExtra("buildingName", placeName);
                        // //We implement startActivity here in order to use placeName
                        // that comes from our fetch response
                        startActivity(intent);

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
