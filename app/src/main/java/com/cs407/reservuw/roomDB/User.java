package com.cs407.reservuw.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo
    public String email;

    @ColumnInfo
    public String password;



    public User(int uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password  = password;
    }

    public User getUser(){
        return this;
    }



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
