package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
     * used in favorites activity to loop and get the latest reservation time available
     */
    @Query("SELECT * FROM reservations WHERE ROOM_uid LIKE :roomUID AND month LIKE :month AND day LIKE :day AND hour LIKE :hour")
    Reservations ifReservationExistAtTime(int roomUID,int month, int day, int hour);


    /**
     * used to get a specific reservation that will be used to delete in myReserv
     */
    @Query("SELECT * FROM reservations WHERE uid LIKE :reservationUID LIMIT 1")
    Reservations getReservationByUID(int reservationUID);

    /**
     * used to display users reservations at a specific day and month in myReserveActivity
     */
    @Query("SELECT * FROM reservations WHERE day LIKE :day AND " +
            "month LIKE :month AND USER_uid LIKE :userUID")
    LiveData<List<Reservations>> getUserReservationByDayMonth(int userUID, int day, int month);

    /**
     * used to get list of <ROOM_uid> that are at a specific building, month, day, and hour. this list
     * will be used in roomsDAO to get a list<Rooms> that excludes room num that are from this list
     */
    @Query("SELECT ROOM_uid FROM reservations WHERE building LIKE :building AND " +
            "day LIKE :day AND month LIKE :month AND hour LIKE :hour")
    List<Integer> getReservationByDayMonthHour(String building , int month ,int day,int hour);

    //get building Rooms that arent reserved at the specific time set

    //get ALL reservations for that building, month, day, and hour
        // you get a list of rooms num off of them
    //query all rooms for that building, excluding the list of room num


}