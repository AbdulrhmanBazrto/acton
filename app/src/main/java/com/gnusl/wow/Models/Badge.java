package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Badge {

    private int id;
    private String path;
    private String path_gray;
    private String type;
    private int price;
    private boolean isGranted;

    public static Badge newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Badge badge = new Badge();

        badge.setId(jsonObject.optInt("id"));
        badge.setPath(jsonObject.optString("path"));
        badge.setPath_gray(jsonObject.optString("path_gray"));
        badge.setType(jsonObject.optString("type"));
        badge.setPrice(jsonObject.optInt("price"));

        return badge;
    }

    public static ArrayList<Badge> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Badge> rooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                rooms.add(Badge.newInstance(jsonArray.getJSONObject(i)));
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

    public String getPath_gray() {
        return path_gray;
    }

    public void setPath_gray(String path_gray) {
        this.path_gray = path_gray;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
