package com.cs407.reservuw.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Rooms.class, Reservations.class, FavoriteRoom.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class uwRoomDatabase extends RoomDatabase {
    public abstract userDAO userDAO();

    public abstract roomDAO roomDAO();

    public abstract reservationDAO reservationDAO();

    public abstract FavoriteRoomDAO FavoriteRoomDAO();



    private volatile static uwRoomDatabase INSTANCE;



    //We've created an ExecutorService with a fixed thread pool
    // that you will use to run database operations asynchronously on a background thread.
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static uwRoomDatabase getDatabase(final Context context) {
        if(INSTANCE==null){
            synchronized(uwRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(
                            context.getApplicationContext(), uwRoomDatabase.class, "appDB")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
