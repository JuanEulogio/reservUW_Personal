package com.cs407.reservuw;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;

import com.cs407.reservuw.recycledViewFiles.MyReserveAdapter;
import com.cs407.reservuw.recycledViewFiles.reservation_item;
import com.cs407.reservuw.roomDB.Reservations;
import com.cs407.reservuw.roomDB.reservationDAO;
import com.cs407.reservuw.roomDB.uwRoomDatabase;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class myReserveActivity extends AppCompatActivity {

    CalendarView calendar;
    SharedPreferences sharedPreferences;

    uwRoomDatabase myDatabase;

    int uid;

    RecyclerView recyclerView;
    List<reservation_item> reservationItems = new ArrayList<>();
    MyReserveAdapter adapter;
    reservationDAO reservationDAO;
    LiveData<List<Reservations>> reservationQuery;

    LinearLayoutManager LinearLayoutManager;
    Button showAllButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reserve);


        myDatabase = Room.databaseBuilder(getApplicationContext(), uwRoomDatabase.class, "my room database")
                .allowMainThreadQueries()
                .build();
        sharedPreferences =
                getSharedPreferences ("com.cs407.reservuw", Context.MODE_PRIVATE);

        uid = sharedPreferences.getInt ("uid", -1);



        //recycleView initialize
        recyclerView = findViewById(R.id.reservedRecyclerView);
        adapter = new MyReserveAdapter(getApplicationContext(), reservationItems);
        reservationDAO = myDatabase.reservationDAO();
        LinearLayoutManager= new LinearLayoutManager(this);

        //re-query button
        Button showAllButton= findViewById(R.id.buttonShowAllReservations);
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setDate(getTodayLongAsDate());
                recycledViewCode(1,1,1);
            }
        });


        //TODO: test code
        /**
        Reservations newReservation1= new Reservations(0, uid, "TEST1: UnionSouth", 1, LocalDateTime.of(2023, 12, 21 , 13, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond()));
        Log.i(TAG, "TEST for our reservation day and month: " + newReservation1.day + " , " + newReservation1.month);
        myDatabase.reservationDAO().insertReservation(newReservation1);

        Reservations newReservation2= new Reservations(0, uid, "TEST2: UnionSouth", 2, LocalDateTime.of(2023, 12, 22 , 14, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond()));
        myDatabase.reservationDAO().insertReservation(newReservation2);

        Reservations newReservation3= new Reservations(0, uid, "TEST3: UnionSouth", 3, LocalDateTime.of(2023, 12, 23 , 3, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond()));
        myDatabase.reservationDAO().insertReservation(newReservation3);

        Reservations newReservation4= new Reservations(0, uid, "TEST4: UnionSouth", 4, LocalDateTime.of(2023, 11, 24 , 4, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond()));
        myDatabase.reservationDAO().insertReservation(newReservation4);

        Reservations newReservation5= new Reservations(0, uid, "TEST5: UnionSouth", 5, LocalDateTime.of(2024, 1, 5 , 5, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond()));
        myDatabase.reservationDAO().insertReservation(newReservation5);
        **/




        //setting back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setCalendar();
        recycledViewCode(1,1,1);
    }

    private void setCalendar(){
        calendar = (CalendarView) findViewById(R.id.calendarView);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth){
                //makes a new query to get by specific day and month
                Log.i(TAG,  dayOfMonth + " " + month+1);
                recycledViewCode(2, dayOfMonth, month+1);

            }
        });
    }


    private void recycledViewCode(int query, int day, int month) {
        //clean up for liveData to show update list
        reservationItems.clear();

        //query 1= showing all reservations. Default behaviour
        //query 2= showing by a specific day
        reservationQuery = reservationDAO.getReservationByUser(uid);
        if(query==2){
            reservationQuery = reservationDAO.getReservationByDayMonth(uid, day, month);
        }


            reservationQuery.observe(this, reservations -> {
                if (reservations != null) {
                    for (Reservations reservation : reservations) {
                        Log.i(TAG, "reservation UID= " + reservation.getUid() +", Building: " + reservation.getBuilding() + ", Room Number: " + reservation.getRoomNum()+ ", timeDate: "+ reservation.getDateTime());
                        reservationItems.add(new reservation_item(reservation.getBuilding(),"Room: " + reservation.getRoomNum(),  reservation.getDateTime(), reservation.getUid()));
                    }
                } else {
                    Log.d(TAG, "reservations are null");
                }

                // Set up RecyclerView after fetching data
                recyclerView.setLayoutManager(LinearLayoutManager);
                recyclerView.setAdapter(adapter);


                // on below line we are creating a method to create item touch helper
                // method for adding swipe to delete functionality.
                // in this we are specifying drag direction and position to right
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        // this method is called
                        // when the item is moved.
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // this method is called when we swipe our item to right direction
                        reservation_item deletedReservation = reservationItems.get(viewHolder.getAdapterPosition());

                        //resets list to let in new liveData
                        reservationItems.clear();

                        //this will remove it from our reservation database
                        Reservations DBdeletedReservation= myDatabase.reservationDAO().getReservationByUID(deletedReservation.getReservationUID());
                        myDatabase.reservationDAO().deleteReservation(DBdeletedReservation);


                        // below line is to display our snackbar with action.
                        Snackbar.make(recyclerView, "Reservation deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // resets list to let in new liveData
                                reservationItems.clear();

                                //adding back to the database
                                myDatabase.reservationDAO().insertReservation(DBdeletedReservation);

                            }
                        }).show();
                    }

                }).attachToRecyclerView(recyclerView);
            });
    }


    //used to set todays date again in our calander when we re-query to show all reservations
    public long getTodayLongAsDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, LocalDateTime.now().getDayOfMonth());
        calendar.set(Calendar.MONTH, LocalDateTime.now().getMonthValue()-1);
        calendar.set(Calendar.YEAR, LocalDateTime.now().getYear());
        return calendar.getTimeInMillis();
    }

}