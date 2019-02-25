package com.gnusl.wow.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Visit {

    // for api

    private int id;
    private String name;
    private String image;
    private String image_url;
    private String birthday;
    private String gender;
    private String countryCode;
    private boolean isFollowed;
    private int level;
    private String date;

    public Visit() {
    }


    public static Visit newInstance(JSONObject jsonObject1) {

        JSONObject jsonObject = jsonObject1.optJSONObject("user_id_from");
        if (jsonObject == null) {
            return null;
        }

        Visit user = new Visit();

        user.setId(jsonObject.optInt("id"));
        user.setName(jsonObject.optString("name"));
        user.setImage(jsonObject.optString("image"));
        user.setImage_url(jsonObject.optString("image_url"));
        user.setBirthday(jsonObject.optString("birthday"));
        user.setGender(jsonObject.optString("gender"));

        if (jsonObject.has("is_followed"))
            user.setFollowed(jsonObject.optBoolean("is_followed"));
        else
            user.setFollowed(false);

        if (jsonObject.has("level"))
            user.setLevel(jsonObject.optInt("level"));
        else
            user.setLevel(0);

        // country code
        if (jsonObject.has("country_code")) {
            try {
                user.setCountryCode(jsonObject.getJSONObject("country_code").optString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        user.setDate(jsonObject1.optString("date"));

        return user;
    }

    public static ArrayList<Visit> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Visit> users = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                users.add(Visit.newInstance(jsonArray.getJSONObject(i)));
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

    // for temp
    private int imageResource;

    public Visit(int imageResource) {
        this.imageResource = imageResource;
    }

    public Visit(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
