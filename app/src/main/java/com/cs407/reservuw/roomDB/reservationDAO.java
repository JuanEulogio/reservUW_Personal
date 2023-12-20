package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface reservationDAO {

    @Insert
    void insertReservation(Reservations reservations);

     /** used in reservation activity
     */
    @Query("SELECT * FROM reservations WHERE USER_uid LIKE :USER_uid")
    LiveData<List<Reservations>> getReservationByUser(int USER_uid);


    //used to get what we should filter in our building view: dateTime
    //1) first we select all room_uid from our reservations with the building we want
    //AND its not a room in a reservation where the date is (date given)
    //@Query("SELECT ROOM_uid FROM Reservations WHERE " +
            //":dateTime NOT LIKE dateTime")
    //String getRoomReservationByDate(LocalDateTime dateTime);



}
