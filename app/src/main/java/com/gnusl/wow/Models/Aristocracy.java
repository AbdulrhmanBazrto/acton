package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Aristocracy {

    private int id;
    private String name;
    private int price;
    private String image;
    private String imageUrl;
    private boolean isSubscribed;
    private List<AristocracyDetails> aristocracyDetails = new ArrayList<>();
    public static Aristocracy currentAristocracy;

    public static Aristocracy newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Aristocracy aristocracy = new Aristocracy();

        aristocracy.setId(jsonObject.optInt("id"));
        aristocracy.setName(jsonObject.optString("name"));
        aristocracy.setImage(jsonObject.optString("image"));
        aristocracy.setImageUrl(jsonObject.optString("image_url"));
        aristocracy.setPrice(jsonObject.optInt("price"));
        aristocracy.setSubscribed(jsonObject.optBoolean("is_subscribed"));
        aristocracy.setAristocracyDetails(AristocracyDetails.parseJSONArray(jsonObject.optJSONArray("aristocracy_detail")));

        if (aristocracy.isSubscribed) {
            currentAristocracy = aristocracy;
        }
        return aristocracy;
    }

    public static ArrayList<Aristocracy> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Aristocracy> aristocracies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                aristocracies.add(Aristocracy.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return aristocracies;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public List<AristocracyDetails> getAristocracyDetails() {
        return aristocracyDetails;
    }

    public void setAristocracyDetails(List<AristocracyDetails> aristocracyDetails) {
        this.aristocracyDetails = aristocracyDetails;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
