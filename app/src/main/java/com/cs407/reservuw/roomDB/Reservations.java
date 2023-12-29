package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

@Entity
public class Reservations {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public int USER_uid;

    @ColumnInfo
    public String building;

    @ColumnInfo
    public int roomNum;

    //the start time, with its date.
    //NOTE: when user gets the time it should only be in hours.
    @ColumnInfo
    public LocalDateTime dateTime;

    @ColumnInfo
    public int day;
    @ColumnInfo
    public int month;


    public Reservations(int uid, int USER_uid, String building, int roomNum, LocalDateTime dateTime) {
        this.uid = uid;
        this.USER_uid = USER_uid;
        this.building= building;
        this.roomNum  = roomNum;
        this.dateTime = dateTime;
        this.day= dateTime.getDayOfMonth();
        this.month= dateTime.getMonthValue();
    }


    public int getRoomNum() {
        return roomNum;
    }

    public String getBuilding() {
        return building;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }


    public int getUid() {
        return uid;
    }
}
