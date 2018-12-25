package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomLockType {

    private int id;
    private String name;
    private int duration;

    public static RoomLockType newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        RoomLockType roomLockType = new RoomLockType();

        roomLockType.setId(jsonObject.optInt("id"));
        roomLockType.setName(jsonObject.optString("name"));
        roomLockType.setDuration(jsonObject.optInt("duration"));

        return roomLockType;
    }

    public static ArrayList<RoomLockType> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<RoomLockType> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(RoomLockType.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
