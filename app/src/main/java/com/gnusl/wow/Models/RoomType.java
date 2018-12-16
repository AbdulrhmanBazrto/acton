package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomType {

    private int id;
    private String name;
    private int cpacity;
    private int member;
    private int level;
    private String icon;
    private String icon_url;

    public static RoomType newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        RoomType roomType = new RoomType();

        roomType.setId(jsonObject.optInt("id"));
        roomType.setName(jsonObject.optString("name"));
        roomType.setCpacity(jsonObject.optInt("cpacity"));
        roomType.setMember(jsonObject.optInt("member"));
        roomType.setLevel(jsonObject.optInt("level"));
        roomType.setIcon(jsonObject.optString("icon"));
        roomType.setIcon_url(jsonObject.optString("icon_url"));

        return roomType;
    }

    public static ArrayList<RoomType> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<RoomType> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(RoomType.newInstance(jsonArray.getJSONObject(i)));
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

    public int getCpacity() {
        return cpacity;
    }

    public void setCpacity(int cpacity) {
        this.cpacity = cpacity;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
