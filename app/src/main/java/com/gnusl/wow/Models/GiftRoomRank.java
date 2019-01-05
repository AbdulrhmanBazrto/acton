package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftRoomRank {

    private String total;
    private Room room;

    public static GiftRoomRank newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        GiftRoomRank giftRoomRank = new GiftRoomRank();

        giftRoomRank.setTotal(jsonObject.optString("total"));

        // user
        if(jsonObject.has("channel")) {
            try {
                giftRoomRank.setRoom(Room.newInstance(jsonObject.getJSONObject("channel")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return giftRoomRank;
    }

    public static ArrayList<GiftRoomRank> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<GiftRoomRank> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(GiftRoomRank.newInstance(jsonArray.getJSONObject(i)));
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
