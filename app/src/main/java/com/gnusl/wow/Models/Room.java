package com.gnusl.wow.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehia on 9/27/2018.
 */

public class Room implements Parcelable {

    private int id;
    private String name;
    private boolean isActive;
    private boolean hasPassword;
    private String password;
    private int userId;
    private int numUsers;
    private boolean isFree;
    private double subscriptionPrice;
    private String backgroundPath;
    private int priority;
    private String createdAt;
    private String updatedAt;
    private String description;
    private String backgroundUrl;
    private String countryCodeUrl;
    private User user;
    private boolean isFollowing;
    private String thumbnailUrl;
    private String tag;
    private int backgroundId;

    public Room() {
    }

    protected Room(Parcel in) {
        id = in.readInt();
        name = in.readString();
        isActive = in.readByte() != 0;
        hasPassword = in.readByte() != 0;
        password = in.readString();
        userId = in.readInt();
        numUsers = in.readInt();
        isFree = in.readByte() != 0;
        subscriptionPrice = in.readDouble();
        backgroundPath = in.readString();
        priority = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        description = in.readString();
        backgroundUrl = in.readString();
        countryCodeUrl = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        isFollowing = in.readByte() != 0;
        thumbnailUrl = in.readString();
        tag = in.readString();
        backgroundId = in.readInt();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public static Room newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Room room = new Room();

        room.setId(jsonObject.optInt("id"));
        room.setName(jsonObject.optString("name"));
        room.setActive(jsonObject.optBoolean("is_active"));
        room.setHasPassword(jsonObject.optInt("has_password") == 1);
        room.setPassword(jsonObject.optString("password"));
        room.setUserId(jsonObject.optInt("user_id"));
        room.setNumUsers(jsonObject.optInt("num_users"));
        room.setFree(jsonObject.optBoolean("is_free"));
        room.setSubscriptionPrice(jsonObject.optDouble("subscription_price"));
        room.setBackgroundPath(jsonObject.optString("background_path"));
        room.setPriority(jsonObject.optInt("priority"));
        room.setCreatedAt(jsonObject.optString("created_at"));
        room.setUpdatedAt(jsonObject.optString("updated_at"));
        room.setDescription(jsonObject.optString("description"));
        room.setBackgroundUrl(jsonObject.optString("background_url"));
        room.setCountryCodeUrl(jsonObject.optString("country_code_url"));
        room.setFollowing(jsonObject.optBoolean("is_follow"));
        room.setThumbnailUrl(jsonObject.optString("thumbnail_url"));
        room.setTag(jsonObject.optString("tag"));
        room.setBackgroundId(jsonObject.optInt("background_id"));

        // user
        if (jsonObject.has("user")) {
            try {
                room.setUser(User.newInstance(jsonObject.getJSONObject("user")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return room;
    }

    public static ArrayList<Room> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Room> rooms = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                rooms.add(Room.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public void setSubscriptionPrice(double subscriptionPrice) {
        this.subscriptionPrice = subscriptionPrice;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getCountryCodeUrl() {
        return countryCodeUrl;
    }

    public void setCountryCodeUrl(String countryCodeUrl) {
        this.countryCodeUrl = countryCodeUrl;
    }

    public int getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(int numUsers) {
        this.numUsers = numUsers;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (isActive ? 1 : 0));
        parcel.writeByte((byte) (hasPassword ? 1 : 0));
        parcel.writeString(password);
        parcel.writeInt(userId);
        parcel.writeInt(numUsers);
        parcel.writeByte((byte) (isFree ? 1 : 0));
        parcel.writeDouble(subscriptionPrice);
        parcel.writeString(backgroundPath);
        parcel.writeInt(priority);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(description);
        parcel.writeString(backgroundUrl);
        parcel.writeString(countryCodeUrl);
        parcel.writeParcelable(user, i);
        parcel.writeByte((byte) (isFollowing ? 1 : 0));
        parcel.writeString(thumbnailUrl);
        parcel.writeString(tag);
        parcel.writeInt(backgroundId);
    }
}