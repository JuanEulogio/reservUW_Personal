package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Rooms {
    @PrimaryKey(autoGenerate = true)
    public int uid;


    @ColumnInfo
    public int roomNumber;


    //String will be the placeID from google places API.
    //not the actual building name. The reason is to make our database match in real time with the
    //building as the buildings being displayed into the MainMenuActivity Map. This makes our building filter picker
    //in buildingActivity syncronize with the mainMenu buildings/Places.
    //The only thing that is hard coded and to keep in mind is our rooms number and its building its tied to.
    //Meaning, if you want to updated the buildings, you have to edit the buildingPlacesID.xml resource file AND the
    //Rooms database, which this sql Entity file is apart of. You change this 'Rooms' Entity building
    //String(which is some places ID) in a sql database maker.
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
