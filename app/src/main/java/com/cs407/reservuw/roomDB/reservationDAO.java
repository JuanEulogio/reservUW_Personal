package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface reservationDAO {

    //used to get users reservations
    @Query("SELECT * FROM reservations WHERE USER_uid LIKE :USER_uid")
    public LiveData<List<Reservations>> getReservationByUser(int USER_uid);

    //used to get what we should filter in our building view building, and dateTime
    //1) first we select all room from our reservations with the building we want
    //AND its not a room in a reservation where the date is (date given)
    //@Query("SELECT ROOM_uid FROM Reservations WHERE building LIKE :building " +
      //      "AND uid NOT IN " +
        //    "(SELECT ROOM_uid FROM reservations WHERE :dateTime IN(SELECT dateTime FROM reservations))")
    //public LiveData<List<Rooms>> getRoomReservationByDate(String building, LocalDateTime dateTime);



}
