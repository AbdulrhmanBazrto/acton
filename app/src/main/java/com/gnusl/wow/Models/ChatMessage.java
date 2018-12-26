package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatMessage {

    private int id;
    private int channel_id;
    private String message;
    private int user_id;
    private String created_at;
    private String updated_at;
    private User user;

    public static ChatMessage newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setId(jsonObject.optInt("id"));
        chatMessage.setChannel_id(jsonObject.optInt("channel_id"));
        chatMessage.setMessage(jsonObject.optString("message"));
        chatMessage.setUser_id(jsonObject.optInt("user_id"));
        chatMessage.setCreated_at(jsonObject.optString("created_at"));
        chatMessage.setUpdated_at(jsonObject.optString("updated_at"));

        // user
        if(jsonObject.has("user")) {
            try {
                chatMessage.setUser(User.newInstance(jsonObject.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return chatMessage;
    }

    public static ArrayList<ChatMessage> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<ChatMessage> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(ChatMessage.newInstance(jsonArray.getJSONObject(i)));
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

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
