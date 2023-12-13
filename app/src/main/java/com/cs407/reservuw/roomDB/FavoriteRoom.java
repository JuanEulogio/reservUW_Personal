package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavoriteRoom {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public int user_uid;

    @ColumnInfo
    public int room_uid;

    public FavoriteRoom(int uid, int user_uid, int room_uid) {
        this.uid = uid;
        this.user_uid = user_uid;
        this.room_uid  = room_uid;
    }


}
