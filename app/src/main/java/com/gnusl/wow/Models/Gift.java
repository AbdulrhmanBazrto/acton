package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Gift {

    private int id;
    private String path;
    private int price;

    public static Gift newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Gift gift = new Gift();

        gift.setId(jsonObject.optInt("id"));
        gift.setPath(jsonObject.optString("path"));
        gift.setPrice(jsonObject.optInt("price"));

        return gift;
    }

    public static ArrayList<Gift> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Gift> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(Gift.newInstance(jsonArray.getJSONObject(i)));
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
