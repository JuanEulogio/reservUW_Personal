package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.cs407.reservuw.roomDB.FavoriteRoomDAO;
import com.cs407.reservuw.roomDB.User;
import com.cs407.reservuw.roomDB.roomDAO;
import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.uwRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();

        FavoriteRoomDAO favoriteDAO = myDatabase.FavoriteRoomDAO();
        roomDAO myRoomDAO = myDatabase.roomDAO();

        int uid = sharedPreferences.getInt ("uid", -1);

        List<Integer> favRoomId = favoriteDAO.getRoomsByBuilding(uid);
        LiveData<List<Rooms>> roomsByRoomId = myRoomDAO.getRoomsByRoomID(favRoomId.toString());


        List<item> items = new ArrayList<>();
        roomsByRoomId.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber());
                    items.add(new item(Integer.toString(room.getRoomNumber()), room.getBuilding(), room.getUid()));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            // Set up RecyclerView after fetching data
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
                    intent.putExtra("roomUID", item.getRoomUID());
                    startActivity(intent);
                }
            });
        });
    }

}
