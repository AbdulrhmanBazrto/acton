package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Background {

    private int id;
    private String name;
    private String path;
    private int price;
    private boolean sold;
    private String backgroundUrl;

    public static Background newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Background badge = new Background();

        badge.setId(jsonObject.optInt("id"));
        badge.setName(jsonObject.optString("name"));
        badge.setPath(jsonObject.optString("path"));
        badge.setPrice(jsonObject.optInt("price"));
        badge.setSold(jsonObject.optBoolean("sold"));
        badge.setBackgroundUrl(jsonObject.optString("background_url"));

        return badge;
    }

    public static ArrayList<Background> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Background> rooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                rooms.add(Background.newInstance(jsonArray.getJSONObject(i)));
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

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }
}
