package com.gnusl.wow.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gnusl.wow.Application.WowApplication;
import com.gnusl.wow.Models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferencesUtils {

    private final static String User = "user";
    private final static String Token = "token";
    private final static String LoginArray = "loginArray";
    private final static String Language = "Language";

    public static void saveUser(User user) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext()).edit();
        Gson gson = new Gson();
        sharedPreferences.putString(User, gson.toJson(user));
        sharedPreferences.apply();

    }

    public static User getUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(User, "");
        if (json == null || json.isEmpty()) {
            return null;
        } else {
            Type type = (new TypeToken<User>() {
            }).getType();
            return (User) gson.fromJson(json, type);
        }
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext()).edit();
        sharedPreferences.putString(Token, token);
        sharedPreferences.apply();

    }

    public static String getToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext());
        return sharedPreferences.getString(Token, "");
    }


    public static void saveLanguage(Context c,String language) {
        SharedPreferences defaultSharedPreferences = c.getSharedPreferences("language",Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferences = defaultSharedPreferences.edit();
        sharedPreferences.clear();
        sharedPreferences.putString(Language, language);
        sharedPreferences.apply();

        Log.d("LANGUAGE in pref set", defaultSharedPreferences.getString(Language, "default"));

    }

    public static String getLanguage(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("language",Context.MODE_PRIVATE);
        Log.d("LANGUAGE in pref get", sharedPreferences.getString(Language, "default"));
        return sharedPreferences.getString(Language, "default");
    }

    public static String getDailyLoginArray() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext());
        return sharedPreferences.getString(LoginArray, "");
    }

    public static void setDailyLoginArray(String loginArray) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext());
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(LoginArray, loginArray);
        edit.apply();
    }

    public static void clear() {
        PreferenceManager.getDefaultSharedPreferences(WowApplication.getAppContext()).edit().clear().apply();
    }
}

