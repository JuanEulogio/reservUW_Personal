package com.cs407.reservuw.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface userDAO {
    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    User findByUser(String email, String password);


    @Insert
    void insertUser(User user);

}
