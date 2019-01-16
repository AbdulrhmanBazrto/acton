package com.gnusl.wow.Delegates;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ConnectionDelegate {

    void onConnectionFailure();

    void onConnectionError(ANError anError);

    void onConnectionSuccess(String response);

    void onConnectionSuccess(JSONObject jsonObject);

    void onConnectionSuccess(JSONArray jsonArray);
}
