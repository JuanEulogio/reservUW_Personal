package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Rooms {
    @PrimaryKey(autoGenerate = true)
    public int uid;


    @ColumnInfo
    public int roomNumber;


    //it will be the placeID from google places API
    //NOTE: hard coded location with resource file.
    @ColumnInfo
    public String building;


    public Rooms(int uid, int roomNumber, String building) {
        this.uid = uid;
        this.roomNumber = roomNumber;
        this.building  = building;
    }



    // getter methods.
    public int getUid() {
        return uid;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getBuilding() {
        return building;
    }
}
