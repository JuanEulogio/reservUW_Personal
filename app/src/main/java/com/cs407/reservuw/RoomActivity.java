package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
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
import com.cs407.reservuw.roomDB.uwRoomDatabase;

public class RoomActivity extends AppCompatActivity {

    boolean isFav = false;

    FavoriteRoom favRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

        uwRoomDatabase myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();

        int uid = sharedPreferences.getInt ("uid", -1);


        //back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Intents
        Intent receivedIntent = getIntent();
        String roomNum = receivedIntent.getStringExtra("roomNum");
        String buildingName = receivedIntent.getStringExtra("buildingName");
        int roomUID = receivedIntent.getIntExtra("roomUID", -1);

        Button cancelButton = findViewById(R.id.cancelButton);
        Button reserveButton = findViewById(R.id.reserveButton);
        TextView roomInfo = findViewById(R.id.boxedTextView);
        ImageButton favButton = findViewById(R.id.likeButton);
        //color the heart if current room is in the fav rooms for current user
        favRoom = myDatabase.FavoriteRoomDAO().getSpecificIfFavorite(uid, roomUID);
        if(favRoom!=null){
            favButton.setColorFilter(0xFFFF0000, PorterDuff.Mode.SRC_IN);
            favButton.invalidate();
            isFav=true;
        }

        String roomInfoText = buildingName + ".\n"
                + roomNum;

        roomInfo.setText(roomInfoText);



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                SharedPreferences sharedPreferences =
                        getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

                int uid = sharedPreferences.getInt ("uid", -1);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), reservationConfirmedActivity.class);
                intent.putExtra("roomNum", roomNum);
                intent.putExtra("buildingName", buildingName);
                startActivity(intent);


                //TODO: make the reservation here
            }
        });




        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "favButton Pressed");

                if(isFav) {
                    Log.i(TAG, "deleted off of fav rooms");
                    favButton.setColorFilter(0xFFAAAAAA, PorterDuff.Mode.SRC_IN);
                    favButton.invalidate();
                    myDatabase.FavoriteRoomDAO().deleteFav(favRoom);
                } else {
                    Log.i(TAG, "insert");
                    favButton.setColorFilter(0xFFFF0000, PorterDuff.Mode.SRC_IN);
                    favButton.invalidate();
                    FavoriteRoom newFavRoom= new FavoriteRoom(0, uid, roomUID);
                    myDatabase.FavoriteRoomDAO().insertNewFav(newFavRoom);
                }
                isFav = !isFav;
            }
        });
    }
}
