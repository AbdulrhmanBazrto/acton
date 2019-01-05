package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftUserRank {

    private String total;
    private User user;

    public static GiftUserRank newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        GiftUserRank giftUserRank = new GiftUserRank();

        giftUserRank.setTotal(jsonObject.optString("total"));

        // user
        if(jsonObject.has("user_id_from")) {
            try {
                giftUserRank.setUser(User.newInstance(jsonObject.getJSONObject("user_id_from")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(jsonObject.has("user_id_to")) {
            try {
                giftUserRank.setUser(User.newInstance(jsonObject.getJSONObject("user_id_to")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return giftUserRank;
    }

    public static ArrayList<GiftUserRank> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<GiftUserRank> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(GiftUserRank.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
