package com.gnusl.wow.Utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gnusl.wow.Application.WowApplication;
import com.gnusl.wow.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferencesUtils {


    public static void saveUser(User user) {
        SharedPreferences.Editor sharedPreferences= PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext()).edit();
        Gson gson = new Gson();
        sharedPreferences.putString("user",gson.toJson(user));
        sharedPreferences.apply();

    }

    public static User getUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        if (json==null || json.isEmpty()) {
            return null;
        } else {
            Type type = (new TypeToken<User>() {}).getType();
            return (User)gson.fromJson(json, type);
        }
    }
}

