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
    //TODO: to query we get the List<Integer> of some other source and then make it into a string
    // which will then be passed here. String should look something like "1, 2, 3, etc."
    @Query("SELECT * FROM Rooms WHERE uid IN (:roomIDs)")
    LiveData<List<Rooms>> getRoomsByRoomID(String roomIDs);


}
