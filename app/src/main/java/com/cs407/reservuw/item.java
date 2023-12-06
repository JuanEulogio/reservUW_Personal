package com.cs407.reservuw;

public class item {
    String building;
    String roomNum;
    Integer image;

    public item(String roomNum, String building, Integer image) {
        this.building = building;
        this.roomNum = roomNum;
        this.image = image;
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

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getImage() {
        return image;
    }

    public void setBuilding(Integer image) {
        this.image = image;
    }
}
