package com.gnusl.wow.Connection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Utils.APIUtils;

import org.json.JSONException;
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

    public static void GetAllChannels(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Get_Channels_Url.getLink()+"?user_id=-1&city_id=-1&order=asc&skip=0&take=10")

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("CHANNELS ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("channels")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("channels"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    connectionDelegate.onConnectionFailure();
                                }
                            }
                            else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("CHANNELS ", anError.getMessage());
                        Log.d("CHANNELS ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetAllFeaturedPosts(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Get_Featured_Posts_Url.getLink()+"?user_id=-1&take=1&skip=0")

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("POSTS ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("posts")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("posts"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    connectionDelegate.onConnectionFailure();
                                }
                            }
                            else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("POSTS ", anError.getMessage());
                        Log.d("POSTS ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateLike(int postId,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Update_Like_Url.getLink()+"?post_id="+postId)

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Like ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Like ", anError.getMessage());
                        Log.d("Like ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetAllComments(int postId,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Get_Comments_Url.getLink()+"?post_id="+postId+"&take=10&skip=0")

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Comments ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("comments")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("comments"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    connectionDelegate.onConnectionFailure();
                                }
                            }
                            else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Comments ", anError.getMessage());
                        Log.d("Comments ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void AddNewComment(String comment,int postId,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Add_Comment_Url.getLink())

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .addBodyParameter("comment", comment)
                .addBodyParameter("post_id", String.valueOf(postId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("New Comment ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("New Comment ", anError.getMessage());
                        Log.d("New Comment ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void CreateNewPost(String description,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Create_Post_Url.getLink())

                .addHeaders("Accept","application/json")
                .addHeaders("Authorization",APIUtils.getAuthorization())
                .addBodyParameter("description", description)
                .addBodyParameter("image", "simon.jpg")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("New Post ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("New Post ", anError.getMessage());
                        Log.d("New Post ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

}
