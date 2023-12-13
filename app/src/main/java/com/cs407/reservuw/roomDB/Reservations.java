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
    public String ROOM_uid;

    //the start time, with its date.
    //NOTE: when user gets the time it should only be in hours.
    @ColumnInfo
    public LocalDateTime dateTime;




    public Reservations(int uid, int USER_uid, String ROOM_uid, LocalDateTime dateTime) {
        this.uid = uid;
        this.USER_uid = USER_uid;
        this.ROOM_uid  = ROOM_uid;
        this.dateTime = dateTime;
    }

}
