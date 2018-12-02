package com.gnusl.wow.Connection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.RegisterParams;

import org.json.JSONObject;

import java.util.Locale;

public class APIConnectionNetwork {


    public static void Login(final String email, final String phone, final String password, final String fcm_token, ConnectionDelegate connectionDelegate) {

        Log.d("LOGIN ",fcm_token!=null ? fcm_token:"");

        AndroidNetworking.post(APILinks.Login_Url.getLink())

                .addBodyParameter(email.isEmpty() ? "mobile" : "email", email.isEmpty() ? phone : email)
                .addBodyParameter("password", password)
                .addBodyParameter("fcm_token", fcm_token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("USER ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (!response.has("status"))
                                connectionDelegate.onConnectionSuccess(response.toString());
                            else
                                connectionDelegate.onConnectionFailure();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }


    public static void LoginBySocial(final RegisterParams params, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Login_Social_Url.getLink())
                .addBodyParameter("name", params.getName())
                .addBodyParameter("email", params.getEmail())
                .addBodyParameter("image", params.getImage() == null ? "" : params.getImage())
                .addBodyParameter("social_id", params.getSocial_id())
                .addBodyParameter("social_type", params.getSocial_type())
                .addBodyParameter("fcm_token", params.getFcm_token())
                .addBodyParameter("language", Locale.getDefault().getLanguage())

                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("USER ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (!response.has("status"))
                                connectionDelegate.onConnectionSuccess(response.toString());
                            else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("ERROR ", anError.getErrorBody());
                        Log.d("ERROR ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void RegisterNewUser(final RegisterParams params, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Register_Url.getLink())
                .addBodyParameter("name", params.getName())
                .addBodyParameter("email", params.getEmail())
                .addBodyParameter("password", params.getPassword())
                .addBodyParameter("c_password", params.getPassword())
                .addBodyParameter("mobile", params.getPhone())
                .addBodyParameter("fcm_token", params.getFcm_token())
                .addBodyParameter("lon", params.getLon())
                .addBodyParameter("lat", params.getLat())
                .addBodyParameter("image", params.getImage() == null ? "" : params.getImage())

                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("USER ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (!response.has("status"))
                                connectionDelegate.onConnectionSuccess(response.toString());
                            else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }


}
