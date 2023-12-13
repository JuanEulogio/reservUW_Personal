package com.cs407.reservuw;

public class item {
    String building;
    String roomNum;
    int roomUID;

    public item(String roomNum, String building, int roomUID) {
        this.building = building;
        this.roomNum = roomNum;
        this.roomUID = roomUID;
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

    public int getRoomUID() { return roomUID; }
}
