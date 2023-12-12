package com.cs407.reservuw.roomDB;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

//TODO:  get help here
public class Converters {
    //for my LocalDateTime
    @TypeConverter
    public static LocalDateTime toDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            return LocalDateTime.parse(dateString);
        }
    }

    @TypeConverter
    public static String toDateString(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toString();
        }
    }


    // for Array list of favorite user rooms
    //TODO: make this works.
    // source: https://stackoverflow.com/questions/49566388/how-to-store-and-retrieve-array-objects-using-room-in-android
    // if all else fails: use date, and time types
    @TypeConverter
    public static ArrayList<Rooms> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Rooms>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Rooms> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

