package com.cs407.reservuw.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteRoomDAO {
    /**
     * used to get rooms via querying a room_uid list and using it on our getRoomsByRoomID()
     * roomDAO query
     */
    @Query("SELECT room_uid FROM FavoriteRoom WHERE user_uid LIKE :userID")
    List<Integer> getRoomsByUserID(int userID);

    /**
     * used on FavoriteActiviy onCreate to know if we should red heart the room, symbolizing
     * this room is a favorite for the user
     */
    @Query("SELECT * FROM FavoriteRoom WHERE user_uid LIKE :userID AND room_uid LIKE :roomID")
    FavoriteRoom getSpecificIfFavorite(int userID, int roomID);

    @Insert
    void insertNewFav(FavoriteRoom favoriteRoom);

    @Delete
    void deleteFav(FavoriteRoom favoriteRoom);

}
