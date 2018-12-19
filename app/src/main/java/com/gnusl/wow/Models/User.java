package com.gnusl.wow.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User implements Parcelable {

    // for api

    private int id;
    private String name;
    private String image;
    private String image_url;

    public User(){}

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        image_url = in.readString();
        imageResource = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        User user = new User();

        user.setId(jsonObject.optInt("id"));
        user.setName(jsonObject.optString("name"));
        user.setImage(jsonObject.optString("image"));
        user.setImage_url(jsonObject.optString("image_url"));

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(image_url);
        dest.writeInt(imageResource);
    }
}
