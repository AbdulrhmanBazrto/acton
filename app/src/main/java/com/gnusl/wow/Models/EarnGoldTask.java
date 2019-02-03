package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EarnGoldTask {

    private int Id;
    private String taskDescription;
    private String iconUrl;
    private int price;
    private boolean isStatusReady;


    public static EarnGoldTask newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        EarnGoldTask earnGoldTask = new EarnGoldTask();

        earnGoldTask.setId(jsonObject.optInt("id"));
        earnGoldTask.setTaskDescription(jsonObject.optString("description_ar"));
        earnGoldTask.setIconUrl(jsonObject.optString("icon_image"));
        earnGoldTask.setPrice(jsonObject.optInt("init_price"));
        earnGoldTask.setStatusReady(jsonObject.optBoolean("withdraw_status"));

        return earnGoldTask;
    }

    public static ArrayList<EarnGoldTask> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<EarnGoldTask> earnGoldTasks=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                earnGoldTasks.add(EarnGoldTask.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return earnGoldTasks;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isStatusReady() {
        return isStatusReady;
    }

    public void setStatusReady(boolean statusReady) {
        isStatusReady = statusReady;
    }
}
