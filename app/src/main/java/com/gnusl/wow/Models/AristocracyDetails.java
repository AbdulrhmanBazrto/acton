package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AristocracyDetails {

    private int id;
    private String name;
    private String imageGray;
    private String image;
    private String description;
    private boolean valid;

    public static AristocracyDetails newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        AristocracyDetails aristocracyDetails = new AristocracyDetails();

        aristocracyDetails.setId(jsonObject.optInt("id"));
        aristocracyDetails.setName(jsonObject.optString("name"));
        aristocracyDetails.setDescription(jsonObject.optString("description"));
        aristocracyDetails.setImageGray(jsonObject.optString("image_gray"));
        aristocracyDetails.setImage(jsonObject.optString("image"));
        int valid = jsonObject.optInt("valid");
        aristocracyDetails.setValid(valid == 1);


        return aristocracyDetails;
    }

    public static ArrayList<AristocracyDetails> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<AristocracyDetails> aristocracyDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                aristocracyDetails.add(AristocracyDetails.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return aristocracyDetails;
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

    public String getImageGray() {
        return imageGray;
    }

    public void setImageGray(String imageGray) {
        this.imageGray = imageGray;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
