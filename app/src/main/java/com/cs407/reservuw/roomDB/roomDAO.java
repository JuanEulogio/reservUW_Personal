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
     * query rooms by building. used when going to building view
     */
    @Query("SELECT * FROM Rooms WHERE building LIKE :building")
    LiveData<List<Rooms>> getRoomsByBuilding(String building);

    /**
     * query rooms by list of room_id. in order to get user favorite rooms
     */
    //TODO: check that my query with roomID is good
    // if not, query one roomID at a time to get Room.
    @Query("SELECT * FROM Rooms WHERE uid IN (:roomID)")
    LiveData<List<Rooms>> getRoomsByRoomID(List<Integer> roomID);


}
