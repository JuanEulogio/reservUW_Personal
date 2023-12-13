/**
package com.cs407.reservuw.roomDB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class myRepository {
    private roomDAO roomDAO;
    private userDAO userDAO;
    private reservationDAO reservationDAO;



    private LiveData<List<Rooms>> getRoomsByBuilding;
    private LiveData<List<Reservations>> getReservationByUser;
    private LiveData<List<Rooms>> getRoomReservationByDate;




    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    myRepository(Application application) {
        uwRoomDatabase db = uwRoomDatabase.getDatabase(application);

        roomDAO = db.roomDAO();
        userDAO= db.userDAO();
        reservationDAO = db.reservationDAO();


        //TODO: way to solve? has parameter and dont know how to handle that here
        getRoomsByBuilding = roomDAO.getRoomsByBuilding();
        getReservationByUser = reservationDAO.getReservationByUser();
        //getRoomReservationByDate = reservationDAO.getRoomReservationByDate();
    }



    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.

    //getRoomByBuilding
    LiveData<List<Rooms>> getGetRoomsByBuilding() {
        return getRoomsByBuilding;
    }

    //getReservationByUser
    LiveData<List<Reservations>> getReservationByUser() {
        return getReservationByUser;
    }

    //getReservationByDateTime
    LiveData<List<Rooms>> getRoomReservationByDate() {
        return getRoomReservationByDate;
    }



    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insertUser(User user) {
        uwRoomDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insertUser(user);
        });
    }
}
 **/