package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {

    // for api

    private int id;
    private String name;
    private String email;
    private String emailVerifiedAt;
    private String createdAt;
    private String updatedAt;
    private String mobile;
    private String type;
    private String ipAddress;
    private String fcmToken;
    private String lastLogin;
    private double lon;
    private double lat;
    private String image;
    private int socialId;
    private String socialType;
    private int cityId;
    private String privacy;
    private String language;

    public User(){}

    public static User newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        User user = new User();

        user.setId(jsonObject.optInt("id"));
        user.setEmail(jsonObject.optString("email"));
        user.setEmailVerifiedAt(jsonObject.optString("email_verified_at"));
        user.setCreatedAt(jsonObject.optString("created_at"));
        user.setUpdatedAt(jsonObject.optString("updated_at"));
        user.setMobile(jsonObject.optString("mobile"));
        user.setType(jsonObject.optString("type"));
        user.setIpAddress(jsonObject.optString("ip_address"));
        user.setFcmToken(jsonObject.optString("fcm_token"));
        user.setLastLogin(jsonObject.optString("last_login"));
        user.setLon(jsonObject.optDouble("lon"));
        user.setLat(jsonObject.optDouble("lat"));
        user.setImage(jsonObject.optString("image"));
        user.setSocialId(jsonObject.optInt("social_id"));
        user.setSocialType(jsonObject.optString("social_type"));
        user.setCityId(jsonObject.optInt("city_id"));
        user.setPrivacy(jsonObject.optString("privacy"));
        user.setLanguage(jsonObject.optString("language"));


        return user;
    }

    public static ArrayList<User> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<User> users=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                users.add(User.newInstance(jsonArray.getJSONObject(i)));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSocialId() {
        return socialId;
    }

    public void setSocialId(int socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    // for temp
    private int imageResource;

    public User(int imageResource) {
        this.imageResource = imageResource;
    }

    public User(int imageResource, String name) {
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
}
