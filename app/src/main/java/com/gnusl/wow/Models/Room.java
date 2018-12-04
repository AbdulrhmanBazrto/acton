package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/27/2018.
 */

public class Room {

    private int id;
    private String name;
    private boolean isActive;
    private boolean hasPassword;
    private String password;
    private int userId;
    private boolean isFree;
    private double subscriptionPrice;
    private String backgroundPath;
    private int priority;
    private String createdAt;
    private String updatedAt;

    public static Room newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Room room = new Room();

        room.setId(jsonObject.optInt("id"));
        room.setName(jsonObject.optString("name"));
        room.setActive(jsonObject.optBoolean("is_active"));
        room.setHasPassword(jsonObject.optBoolean("has_password"));
        room.setPassword(jsonObject.optString("password"));
        room.setUserId(jsonObject.optInt("user_id"));
        room.setFree(jsonObject.optBoolean("is_free"));
        room.setSubscriptionPrice(jsonObject.optDouble("subscription_price"));
        room.setBackgroundPath(jsonObject.optString("background_path"));
        room.setPriority(jsonObject.optInt("priority"));
        room.setCreatedAt(jsonObject.optString("created_at"));
        room.setUpdatedAt(jsonObject.optString("updated_at"));

        return room;
    }

    public static ArrayList<Room> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Room> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(Room.newInstance(jsonArray.getJSONObject(i)));
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}