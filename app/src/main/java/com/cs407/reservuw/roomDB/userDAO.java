package com.cs407.reservuw.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface userDAO {

    /**
     * Our authentication.
     * Steps When someone tries to login:
     * query check to see if the user exist.
     * If he does: TODO: dont do anything? or is this our preference data?
     * Else if he doesnt: insert user
     * @param email
     * @param password
     * @return
     */
    @Query("SELECT * FROM user WHERE email LIKE :email AND " +
            "password LIKE :password LIMIT 1")
    User findByUser(String email, String password);


    @Insert
    void insertUser(User user);



}
