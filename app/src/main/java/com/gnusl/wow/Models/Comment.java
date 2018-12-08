package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Comment {

    private int id;
    private String comment;
    private int userId;
    private int postId;
    private String createdAt;
    private String updatedAt;
    private User user;

    public static Comment newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Comment comment = new Comment();

        comment.setId(jsonObject.optInt("id"));
        comment.setComment(jsonObject.optString("comment"));
        comment.setUserId(jsonObject.optInt("user_id"));
        comment.setPostId(jsonObject.optInt("post_id"));
        comment.setCreatedAt(jsonObject.optString("created_at"));
        comment.setUpdatedAt(jsonObject.optString("updated_at"));

        // user
        if(jsonObject.has("user")) {
            try {
                comment.setUser(User.newInstance(jsonObject.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return comment;
    }

    public static ArrayList<Comment> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Comment> posts=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                posts.add(Comment.newInstance(jsonArray.getJSONObject(i)));
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
