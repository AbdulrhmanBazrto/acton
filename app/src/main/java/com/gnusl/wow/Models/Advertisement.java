package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Advertisement {

    private int id;
    private String created_at;
    private String image;
    private String image_url;

    public static Advertisement newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Advertisement advertisement = new Advertisement();

        advertisement.setId(jsonObject.optInt("id"));
        advertisement.setImage(jsonObject.optString("image"));
        advertisement.setImage_url(jsonObject.optString("image_url"));
        advertisement.setCreated_at(jsonObject.optString("created_at"));

        return advertisement;
    }

    public static ArrayList<Advertisement> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Advertisement> users=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                users.add(Advertisement.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
