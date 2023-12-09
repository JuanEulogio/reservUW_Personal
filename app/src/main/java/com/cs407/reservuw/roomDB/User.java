package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public String email;

    @ColumnInfo
    public String password;


    public User(int uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password  = password;
    }
}
