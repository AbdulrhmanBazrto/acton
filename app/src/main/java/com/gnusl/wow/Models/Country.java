package com.gnusl.wow.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class Country {

    private String iso;
    private String name;
    private String country_code_url;

    public static Country newInstance(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Country country = new Country();

        country.setIso(jsonObject.optString("iso"));
        country.setName(jsonObject.optString("name"));
        country.setCountry_code_url(jsonObject.optString("country_code_url"));

        return country;
    }

    public static ArrayList<Country> parseJSONArray(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }

        ArrayList<Country> rooms=new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++) {
            try {
                rooms.add(Country.newInstance(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rooms;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry_code_url() {
        return country_code_url;
    }

    public void setCountry_code_url(String country_code_url) {
        this.country_code_url = country_code_url;
    }
}
