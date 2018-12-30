package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SystemMessage {

    private int id;
    private String systemMessage;
    private String created_at;

    public static SystemMessage newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        SystemMessage systemMessage = new SystemMessage();

        systemMessage.setId(jsonObject.optInt("id"));
        systemMessage.setSystemMessage(jsonObject.optString("message"));
        systemMessage.setCreated_at(jsonObject.optString("created_at"));

        return systemMessage;
    }

    public static ArrayList<SystemMessage> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<SystemMessage> messages=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                messages.add(SystemMessage.newInstance(jsonArray.getJSONObject(i)));
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

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
