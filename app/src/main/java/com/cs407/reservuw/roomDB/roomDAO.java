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
     * query rooms by building
     */
    @Query("SELECT * FROM Rooms WHERE building LIKE :building")
    LiveData<List<Rooms>> getRoomsByBuilding(String building);

    //How to query filter building view?
    //1) start our building view by doing a query of all rooms of that building
    //2) then query all reservation at the specified time/date(default is next current hour)
    //3) do a for loop comparing these list, if a room matches, remove it from our room list we will
    // display to the user in the recycled view

    //UPDATE: might have done a query that does this all for us

}
