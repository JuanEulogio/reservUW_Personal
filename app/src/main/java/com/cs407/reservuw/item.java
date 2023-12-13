package com.cs407.reservuw;

public class item {
    String building;
    String roomNum;

    public item(String roomNum, String building) {
        this.building = building;
        this.roomNum = roomNum;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomNum() {
        return roomNum;
    }
}
