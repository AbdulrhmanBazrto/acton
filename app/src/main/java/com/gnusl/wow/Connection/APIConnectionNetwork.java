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

import java.io.File;
import java.util.Locale;

public class APIConnectionNetwork {


    public static void Login(final String email, final String phone, final String password, final String fcm_token, ConnectionDelegate connectionDelegate) {

        Log.d("LOGIN ", fcm_token != null ? fcm_token : "");

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

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "?user_id=-1&city_id=-1&order=asc&skip=0&take=10")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
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
                            } else
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

        AndroidNetworking.get(APILinks.Featured_Posts_Url.getLink() + "?user_id=-1&take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
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
                            } else
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

    public static void UpdateLike(int postId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Update_Like_Url.getLink() + "?post_id=" + postId)

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
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

    public static void GetAllComments(int postId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Comments_Url.getLink() + "?post_id=" + postId + "&take=10&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
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
                            } else
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

    public static void AddNewComment(String comment, int postId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Add_Comment_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
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

    public static void CreateNewPost(String description, String imageName, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Featured_Posts_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("description", description)
                .addBodyParameter("image", imageName != null ? imageName : "simon.jpg")
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

    public static void UploadImage(File imageFile, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.upload(APILinks.Upload_Image_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addMultipartFile("file", imageFile)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Upload Image ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload Image ", anError.getMessage());
                        Log.d("Upload Image ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetChannelsTypes(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Get_Channels_Type_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Channels Types ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("channel_types")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("channel_types"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    connectionDelegate.onConnectionFailure();
                                }
                            } else
                                connectionDelegate.onConnectionFailure();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Channels Types ", anError.getMessage());
                        Log.d("Channels Types ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void CreateNewRoom(int roomTypeId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Channels_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("channel_type_id", String.valueOf(roomTypeId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Create Channel ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Create Channel ", anError.getMessage());
                        Log.d("Create Channel ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateComment(int commentId, String content,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Comments_Url.getLink()+"?comment_id="+String.valueOf(commentId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type","application/x-www-form-urlencoded")
                .addBodyParameter("comment",content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Update Comment ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Update Comment ", anError.getMessage());
                        Log.d("Update Comment ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void DeleteComment(int commentId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.delete(APILinks.Comments_Url.getLink()+"?comment_id="+String.valueOf(commentId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type","application/x-www-form-urlencoded")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Delete Comment ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Delete Comment ", anError.getMessage());
                        Log.d("Delete Comment ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdatePost(int postId, String description,ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Comments_Url.getLink()+"?comment_id="+String.valueOf(postId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type","application/x-www-form-urlencoded")
                //.addBodyParameter("comment",content)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Update Comment ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Update Comment ", anError.getMessage());
                        Log.d("Update Comment ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void DeletePost(int postId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.delete(APILinks.Featured_Posts_Url.getLink()+"?post_id="+String.valueOf(postId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type","application/x-www-form-urlencoded")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Delete Post ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Delete Post ", anError.getMessage());
                        Log.d("Delete Post", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

}
