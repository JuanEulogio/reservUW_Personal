package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface reservationDAO {

    @Insert
    void insertReservation(Reservations reservations);

    @Delete
    void deleteReservation(Reservations reservations);


     /** used in reservation activity
     */
    @Query("SELECT * FROM reservations WHERE USER_uid LIKE :USER_uid")
    LiveData<List<Reservations>> getReservationByUser(int USER_uid);


    /**
     * used to get a specific reservation that will be used to delete in myReserv
     */
    @Query("SELECT * FROM reservations WHERE uid LIKE :reservationUID LIMIT 1")
    Reservations getReservationByUID(int reservationUID);

    /**
     * used reservation by given date
     */
    @Query("SELECT * FROM reservations WHERE day LIKE :day AND " +
            "month LIKE :month AND USER_uid LIKE :userUID")
    LiveData<List<Reservations>> getReservationByDayMonth(int userUID, int day, int month);

}
