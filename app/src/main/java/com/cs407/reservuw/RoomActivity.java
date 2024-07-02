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
import com.cs407.reservuw.roomDB.Reservations;
import com.cs407.reservuw.roomDB.reservationDAO;
import com.cs407.reservuw.roomDB.uwRoomDatabase;

import java.time.LocalDateTime;

public class RoomActivity extends AppCompatActivity {

    boolean isFav = false;

    FavoriteRoom favRoom;

    SharedPreferences sharedPreferences;
    uwRoomDatabase myDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //rendering activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        //TODO: should i and how do i not do mainThreadQueries?
        myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();

       sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

       //TODO: doesnt seem needed because not use later unless i make it into a global variable
        int uid = sharedPreferences.getInt ("uid", -1);



        //TODO: make function get -> setting all intents
        //Intents
        Intent receivedIntent = getIntent();
        String roomNum = receivedIntent.getStringExtra("roomNum");
        String buildingName = receivedIntent.getStringExtra("buildingName");
        int roomUID = receivedIntent.getIntExtra("roomUID", -1);
        int month= receivedIntent.getIntExtra("month", -1);
        int day= receivedIntent.getIntExtra("day", -1);
        int hour= receivedIntent.getIntExtra("hour", -1);
        //TODO: year addition here





        //TODO: make function making all ui elements
        TextView roomInfo = findViewById(R.id.boxedTextView);
        String roomInfoText = buildingName + ".\n"
                + roomNum + ".\n"
                + "Date: " + month + "/" + day+ ".\n"
                + "Time: " + hour;

        roomInfo.setText(roomInfoText);


        //TODO: Make function activate for each onclick.
        //buttons

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button cancelButton = findViewById(R.id.cancelButton);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: shouldnt cancel button go back to the buildingView?
                // The only reason not is because the person got to roomActivity because of favRooms
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                //TODO: cant i use the global variable?
                SharedPreferences sharedPreferences =
                        getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

                int uid = sharedPreferences.getInt ("uid", -1);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        Button reserveButton = findViewById(R.id.reserveButton);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: should i parse it before sending it over?
                //removes the 'Room: ' sting that was used for recycled view
                int roomNumber= Integer.parseInt(roomNum.substring(6,roomNum.length()));

                myDatabase.reservationDAO().insertReservation(new Reservations(0, uid, buildingName, roomNumber, roomUID, LocalDateTime.of(LocalDateTime.now().getYear(), month, day, hour, 0)));

                //to reservation confirm activity
                Intent intent = new Intent(getApplicationContext(), reservationConfirmedActivity.class);
                intent.putExtra("textConfirm", roomInfoText);

                startActivity(intent);
            }
        });



        ImageButton favButton = findViewById(R.id.likeButton);
        //color the heart if current room is in the fav rooms for current user
        favRoom = myDatabase.FavoriteRoomDAO().getSpecificIfFavorite(uid, roomUID);
        //TODO: what specific info comes out of this query? having null seems vugue for if statement
        if(favRoom!=null){
            favButton.setColorFilter(getColor(R.color.UWcolor), PorterDuff.Mode.SRC_IN);
            favButton.invalidate(); //calls system to update the icons color
            isFav=true;
        }
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFav) {
                    //removes from room favorites
                    favButton.setColorFilter(getColor(R.color.dimGray), PorterDuff.Mode.SRC_IN);
                    favButton.invalidate();
                    myDatabase.FavoriteRoomDAO().deleteFav(favRoom);

                } else {
                    //adds into favorites
                    favButton.setColorFilter(getColor(R.color.UWcolor), PorterDuff.Mode.SRC_IN);
                    favButton.invalidate();
                    FavoriteRoom newFavRoom= new FavoriteRoom(0, uid, roomUID);
                    myDatabase.FavoriteRoomDAO().insertNewFav(newFavRoom);
                    favRoom= newFavRoom;
                }

                isFav = !isFav;
            }
        });


    }
}
