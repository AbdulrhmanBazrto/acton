package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Message {

    private int id;
    private int user_id_from;
    private int user_id_to;
    private String message;
    private String created_at;

    public static Message newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Message message = new Message();

        message.setId(jsonObject.optInt("id"));
        message.setUser_id_from(jsonObject.optInt("user_id_from"));
        message.setUser_id_to(jsonObject.optInt("user_id_to"));
        message.setMessage(jsonObject.optString("message"));
        message.setCreated_at(jsonObject.optString("created_at"));

        return message;
    }

    public static ArrayList<Message> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Message> messages=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                messages.add(Message.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id_from() {
        return user_id_from;
    }

    public void setUser_id_from(int user_id_from) {
        this.user_id_from = user_id_from;
    }

    public int getUser_id_to() {
        return user_id_to;
    }

    public void setUser_id_to(int user_id_to) {
        this.user_id_to = user_id_to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
