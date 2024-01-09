package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface roomDAO {
    /**
     * query rooms by list of room_id. in order to get user favorite rooms
     */
    @Query("SELECT * FROM Rooms WHERE uid IN (:room_id)")
    LiveData<List<Rooms>> getRoomsByRoomID(List<Integer> room_id);


    /**
     * queries list<Rooms> from a building, that excludes that are from roomID list parameter
     * (from getReservationByDayMonthHour)
     */
    @Query("SELECT * FROM Rooms WHERE building LIKE :building AND uid NOT IN (:roomID)")
    LiveData<List<Rooms>> getRoomsByBuildingMonthDayHour(String building, List<Integer> roomID);


}
