package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cs407.reservuw.roomDB.FavoriteRoom;
import com.cs407.reservuw.roomDB.FavoriteRoomDAO;
import com.cs407.reservuw.roomDB.Rooms;
import com.cs407.reservuw.roomDB.roomDAO;
import com.cs407.reservuw.roomDB.uwRoomDatabase;

import java.util.List;

public class roomView extends AppCompatActivity {

    boolean isFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();

        FavoriteRoomDAO favoriteDAO = myDatabase.FavoriteRoomDAO();
        roomDAO myRoomDAO = myDatabase.roomDAO();

        int uid = sharedPreferences.getInt ("uid", -1);

        List<Integer> favRoomId = favoriteDAO.getRoomsByBuilding(uid);
        LiveData<List<Rooms>> roomsByRoomId = myRoomDAO.getRoomsByRoomID(favRoomId.toString());

        Button cancelButton = findViewById(R.id.cancelButton);
        Button reserveButton = findViewById(R.id.reserveButton);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView roomInfo = findViewById(R.id.boxedTextView);
        ImageButton favButton = findViewById(R.id.likeButton);

        Intent receivedIntent = getIntent();
        String roomNum = receivedIntent.getStringExtra("roomNum");
        String buildingName = receivedIntent.getStringExtra("buildingName");
        int roomUID = receivedIntent.getIntExtra("roomUID", -1);

        String roomInfoText = "This room is located at " + buildingName + ".\n"
                + "Room Number: " + roomNum;

        roomInfo.setText(roomInfoText);

        roomsByRoomId.observe(this, rooms -> {
            if (rooms != null) {
                for (Rooms room : rooms) {
                    Log.d(TAG, "Building: " + room.getBuilding() + ", Room Number: " + room.getRoomNumber());

                    if (Integer.toString(room.getRoomNumber()).equals(roomNum)) {
                        isFav = true;


                    }
                }
            } else {
                Log.d(TAG, "Rooms are null");
            }



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
                }
            });


            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "favButton Pressed");

                    //TODO: comment by juan to Jay: this might be where you got the error.

                    FavoriteRoom favRoom = new FavoriteRoom(0, uid, roomUID);
                    if(isFav) {
                        Log.i(TAG, "delete");
                        favButton.setColorFilter(0xFFAAAAAA, PorterDuff.Mode.SRC_IN);
                        favButton.invalidate();
                        myDatabase.FavoriteRoomDAO().deleteFav(favRoom);
                    } else {
                        Log.i(TAG, "insert");
                        favButton.setColorFilter(0xFFFF0000, PorterDuff.Mode.SRC_IN);
                        favButton.invalidate();
                        myDatabase.FavoriteRoomDAO().insertNewFav(favRoom);
                    }
                    isFav = !isFav;
                }
            });

        });
    }
}