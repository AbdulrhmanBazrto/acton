package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Badge {

    private int id;
    private String path;
    private int price;

    public static Badge newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Badge badge = new Badge();

        badge.setId(jsonObject.optInt("id"));
        badge.setPath(jsonObject.optString("path"));
        badge.setPrice(jsonObject.optInt("price"));

        return badge;
    }

    public static ArrayList<Badge> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Badge> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
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
}
