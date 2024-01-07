package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

    @ColumnInfo
    public int ROOM_uid;

    //the start time, with its date.
    //NOTE: when user gets the time it should only be in hours.
    @ColumnInfo
    public LocalDateTime dateTime;

    @ColumnInfo
    public int day;
    @ColumnInfo
    public int month;

    @ColumnInfo
    public int hour;


    public Reservations(int uid, int USER_uid, String building, int roomNum, int ROOM_uid, LocalDateTime dateTime) {
        this.uid = uid;
        this.USER_uid = USER_uid;
        this.building= building;
        this.roomNum  = roomNum;
        this.ROOM_uid= ROOM_uid;

        this.dateTime = dateTime;
        this.day= dateTime.getDayOfMonth();
        this.month= dateTime.getMonthValue();
        this.hour= dateTime.getHour();
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
