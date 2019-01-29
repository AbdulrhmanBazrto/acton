package com.gnusl.wow.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yehia on 10/1/2018.
 */

public class FeaturePost implements Parcelable {

    private int id;
    private int userId;
    private String description;
    private String image;
    private String createdAt;
    private String updatedAt;
    private int numLikes;
    private int numComments;
    private boolean isLiked;
    private User user;

    public FeaturePost(){}

    protected FeaturePost(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        description = in.readString();
        image = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        numLikes = in.readInt();
        numComments = in.readInt();
        isLiked = in.readByte() != 0;
    }

    public static final Creator<FeaturePost> CREATOR = new Creator<FeaturePost>() {
        @Override
        public FeaturePost createFromParcel(Parcel in) {
            return new FeaturePost(in);
        }

        @Override
        public FeaturePost[] newArray(int size) {
            return new FeaturePost[size];
        }
    };

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
        post.setLiked(jsonObject.optBoolean("is_liked"));
        post.setNumComments(jsonObject.optInt("num_comments"));

        // user
        if(jsonObject.has("user")) {
            try {
                post.setUser(User.newInstance(jsonObject.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(numLikes);
        dest.writeInt(numComments);
        dest.writeByte((byte) (isLiked ? 1 : 0));
    }
}
