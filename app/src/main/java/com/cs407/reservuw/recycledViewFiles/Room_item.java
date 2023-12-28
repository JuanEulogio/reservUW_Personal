package com.cs407.reservuw.recycledViewFiles;

public class Room_item {
    String building;
    String roomNum;
    int roomUID;

    public Room_item(String roomNum, String building, int roomUID) {
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
