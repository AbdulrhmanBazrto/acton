package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecialID {

    private int id;
    private String txtId;
    private double price;

    public static SpecialID newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        SpecialID id = new SpecialID();
        id.setId(jsonObject.optInt("id"));
        id.setTxtId(jsonObject.optString("txt_id"));
        id.setPrice(jsonObject.optDouble("price"));

        return id;
    }

    public static ArrayList<SpecialID> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<SpecialID> ids = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ids.add(SpecialID.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ids;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxtId() {
        return txtId;
    }

    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
