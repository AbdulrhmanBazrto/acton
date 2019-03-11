package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MicUser {

    private int micId;
    private User user;
    private String type;

    public static MicUser newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        MicUser micUser = new MicUser();
        micUser.setMicId(jsonObject.optInt("mic"));
        micUser.setType(jsonObject.optString("type"));

        // user
        if (jsonObject.has("user")) {
            try {
                micUser.setUser(User.newInstance(jsonObject.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return micUser;
    }

    public static ArrayList<MicUser> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<MicUser> rooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                rooms.add(MicUser.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public int getMicId() {
        return micId;
    }

    public void setMicId(int micId) {
        this.micId = micId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
