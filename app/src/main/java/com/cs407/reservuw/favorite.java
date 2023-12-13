package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import android.content.Context;
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

        Log.i(TAG, "got sharedPreference");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .build();

        FavoriteRoomDAO favoriteDAO = myDatabase.FavoriteRoomDAO();
        roomDAO myRoomDAO = myDatabase.roomDAO();

        int uid = sharedPreferences.getInt ("uid", -1);

        Log.i(TAG, "right before getRoomsByBuilding(uid)" + Integer.toString(uid));
        List<Integer> favRoomId = favoriteDAO.getRoomsByBuilding(uid);
        Log.i(TAG, "got getRoomsByBuilding(uid)");
        LiveData<List<Rooms>> roomsByRoomId = myRoomDAO.getRoomsByRoomID(favRoomId);


        List<item> items = new ArrayList<>();
        roomsByRoomId.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber());
                    items.add(new item(Integer.toString(room.getRoomNumber()), room.getBuilding()));
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }

            // Set up RecyclerView after fetching data
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new BuildingAdapter(getApplicationContext(), items));

            ImageButton backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        });
    }

}
