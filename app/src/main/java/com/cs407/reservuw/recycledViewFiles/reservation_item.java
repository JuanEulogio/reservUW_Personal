package com.cs407.reservuw.recycledViewFiles;


import java.time.LocalDateTime;

public class reservation_item {
    String building;
    String roomNum;
    LocalDateTime timeDate;

    int reservationUID;


    public reservation_item(String building, String roomNum, LocalDateTime timeDate, int reservationUID) {
        this.building = building;
        this.roomNum = roomNum;
        this.timeDate = timeDate;
        this.reservationUID = reservationUID;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public LocalDateTime getTimeDate() { return timeDate; }

    public int getReservationUID() { return reservationUID; }

}
