package com.gnusl.wow.Delegates;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ConnectionDelegate {

    public void onConnectionFailure();

    public void onConnectionError(ANError anError);

    public void onConnectionSuccess(String response);

    public void onConnectionSuccess(JSONObject jsonObject);

    public void onConnectionSuccess(JSONArray jsonArray);
}
