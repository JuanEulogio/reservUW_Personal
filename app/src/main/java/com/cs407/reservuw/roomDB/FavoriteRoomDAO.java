package com.cs407.reservuw.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteRoomDAO {
    /**
     * query room_uid by user_uid. Used to get rooms via this room_uid list query
     *
     * NOTE: mistake in name. accident. just work with it
     */
    @Query("SELECT room_uid FROM FavoriteRoom WHERE user_uid LIKE :userID")
    List<Integer> getRoomsByUserID(int userID);

    @Query("SELECT * FROM FavoriteRoom WHERE user_uid LIKE :userID AND room_uid LIKE :roomID")
    FavoriteRoom getSpecificIfFavorite(int userID, int roomID);

    @Insert
    void insertNewFav(FavoriteRoom favoriteRoom);

    @Delete
    void deleteFav(FavoriteRoom favoriteRoom);

}
