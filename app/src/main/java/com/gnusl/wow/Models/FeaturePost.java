package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yehia on 10/1/2018.
 */

public class FeaturePost {

    private int id;
    private int userId;
    private String description;
    private String image;
    private String createdAt;
    private String updatedAt;
    private int numLikes;
    private int numComments;

    public static FeaturePost newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        FeaturePost post = new FeaturePost();

        post.setId(jsonObject.optInt("id"));
        post.setUserId(jsonObject.optInt("user_id"));
        post.setDescription(jsonObject.optString("description"));
        post.setImage(jsonObject.optString("image"));
        post.setCreatedAt(jsonObject.optString("created_at"));
        post.setUpdatedAt(jsonObject.optString("updated_at"));
        post.setNumLikes(jsonObject.optInt("num_likes"));
        post.setNumComments(jsonObject.optInt("num_comments"));

        return post;
    }

    public static ArrayList<FeaturePost> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<FeaturePost> posts=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                posts.add(FeaturePost.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(int numLikes) {
        this.numLikes = numLikes;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }
}
