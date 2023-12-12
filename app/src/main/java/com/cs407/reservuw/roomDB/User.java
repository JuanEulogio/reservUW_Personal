package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    public String email;

    @ColumnInfo
    public String password;


    //TODO: finish this implemention
    //@ColumnInfo
    //public List<Rooms> favoriteRooms;


    public User(int uid, String email, String password) {
        //, List<Rooms> favoriteRooms
        this.uid = uid;
        this.email = email;
        this.password  = password;
        //this.favoriteRooms= favoriteRooms;
    }

    //used to get users favorite rooms.
    //TODO: if we get this, we should be able to freely edit with it and the changes are reflected
    // in here as well. just say arrlist= getFavoriteRooms()
    // set= get our list, and add the room to it
    //public List<Rooms> getFavoriteRooms() {
        //return favoriteRooms;
    //}


    public int getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
