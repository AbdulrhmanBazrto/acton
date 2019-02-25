package com.gnusl.wow.Connection;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.UserAttendanceType;
import com.gnusl.wow.Models.RegisterParams;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.WebRtcClient.CallDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class APIConnectionNetwork {


    public static void Login(final String email, final String phone, final String password, final String fcm_token, ConnectionDelegate connectionDelegate) {

        Log.d("LOGIN ", fcm_token != null ? fcm_token : "");

        AndroidNetworking.post(APILinks.Login_Url.getLink())
                .addHeaders("Content-type", "application/javascript")
                .addBodyParameter(email.isEmpty() ? "mobile" : "email", email.isEmpty() ? phone : email)
                .addBodyParameter("password", password)
                //.addBodyParameter("fcm_token", fcm_token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("USER ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("data")) {

                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONObject("data"));
                                } catch (JSONException e) {
                                    connectionDelegate.onConnectionFailure();
                                    e.printStackTrace();
                                }
                            } else
                                connectionDelegate.onConnectionFailure();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        if (anError.getErrorBody() != null)
                            Log.d("USER ", anError.getErrorBody());

                        if (anError.getErrorDetail() != null)
                            Log.d("USER ", anError.getErrorDetail());

                        if (anError.getResponse() != null)
                            Log.d("USER ", anError.getResponse().message());

                        if (anError.getResponse() != null) {
                            try {
                                Log.d("USER ", anError.getResponse().body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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
                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("success")) {

                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONObject("success"));
                                } catch (JSONException e) {
                                    connectionDelegate.onConnectionFailure();
                                    e.printStackTrace();
                                }
                            } else
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
                .addHeaders("Accept", "application/json")
                .addBodyParameter("name", params.getName())
                //.addBodyParameter("email", params.getEmail())
                .addBodyParameter("password", params.getPassword())
                .addBodyParameter("c_password", params.getPassword())
                .addBodyParameter("mobile", params.getPhone())
                .addBodyParameter("fcm_token", params.getFcm_token())
                .addBodyParameter("lon", params.getLon())
                .addBodyParameter("lat", params.getLat())

                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("USER ", response.toString());

                        // handle parse user data
                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("success")) {

                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONObject("success"));
                                } catch (JSONException e) {
                                    connectionDelegate.onConnectionFailure();
                                    e.printStackTrace();
                                }
                            } else
                                connectionDelegate.onConnectionFailure();

                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        if (anError.getErrorBody() != null)
                            Log.d("USER ", anError.getErrorBody());

                        if (anError.getErrorDetail() != null)
                            Log.d("USER ", anError.getErrorDetail());

                        if (anError.getResponse() != null)
                            Log.d("USER ", anError.getResponse().message());

                        if (anError.getResponse() != null) {
                            try {
                                Log.d("USER ", anError.getResponse().body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }

    public static void GetAllChannels(String messageFilter, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "?user_id=" + String.valueOf(messageFilter != null ? messageFilter : -1) + "&city_id=-1&order=asc&skip=0&take=10")

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

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("CHANNELS ", anError.getMessage());
                        Log.d("CHANNELS ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetAllFeaturedPosts(int userId, int take, int skip, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Featured_Posts_Url.getLink() + "?user_id=" + -1 + "&take=" + take + "&skip=" + skip)

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

    public static void GetAllFollowingPosts(int take, int skip, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Posts_By_Following.getLink() + "?take=" + take + "&skip=" + skip)

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Following POSTS ", response.toString());

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

                        Log.d("Following POSTS ", anError.getMessage());
                        Log.d("Following POSTS ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetMyPosts(int take, int skip, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_API_Url.getLink() + "post?take=" + take + "&skip=" + skip)

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Following POSTS ", response.toString());

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

                        Log.d("Following POSTS ", anError.getMessage());
                        Log.d("Following POSTS ", anError.getErrorDetail());

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

        AndroidNetworking.post(APILinks.Comments_Url.getLink())

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

    public static void UpdateComment(int commentId, String content, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Comments_Url.getLink() + "?comment_id=" + String.valueOf(commentId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("comment", content)
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

        AndroidNetworking.delete(APILinks.Comments_Url.getLink() + "?comment_id=" + String.valueOf(commentId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
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

    public static void UpdatePost(int postId, String description, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Featured_Posts_Url.getLink() + "?post_id=" + String.valueOf(postId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("description", description)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Update Post ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Update Post ", anError.getMessage());
                        Log.d("Update Post ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void DeletePost(int postId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.delete(APILinks.Featured_Posts_Url.getLink() + "?post_id=" + String.valueOf(postId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
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

    public static void SendMessageByChannel(String message, int channelId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Channels_Url.getLink() + "/" + String.valueOf(channelId) + "/" + "message")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("message", message)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Send MessageSection", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Send MessageSection ", anError.getMessage());
                        Log.d("Send MessageSection ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void ChangeRoomBackground(String backroundName, int channelId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Channels_Url.getLink() + "?channel_id=" + String.valueOf(channelId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("background_path", backroundName)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change Background", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Change Background ", anError.getMessage());
                        Log.d("Change Background ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void ChangeRoomName(String name, int channelId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Channels_Url.getLink() + "?channel_id=" + String.valueOf(channelId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("name", name)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change Name", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Change Name ", anError.getMessage());
                        Log.d("Change Name ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void SetRoomPassWord(String password, int channelId, int channelTypeId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Channels_Url.getLink() + "?channel_id=" + String.valueOf(channelId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("is_active", "0")
                .addBodyParameter("has_password", "1")
                .addBodyParameter("password", password)
                .addBodyParameter("channel_type_id", String.valueOf(channelTypeId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Set Password", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Set Password ", anError.getMessage());
                        Log.d("Set Password ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetRoomLockType(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Lock_Type_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Lock Type ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("lock_types")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("lock_types"));
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

                        Log.d("Lock Type ", anError.getMessage());
                        Log.d("Lock Type ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetAllRoomMessages(int channelId, int take, int skip, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/" + String.valueOf(channelId) + "/message?take=" + take + "&skip=" + skip)

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Room Messages ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("messages")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("messages"));
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

                        Log.d("Room Messages ", anError.getMessage());
                        Log.d("Room Messages ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetStreamClients(final int channelId, final String callId, final CallDelegate callDelegate) {

        AndroidNetworking.get(APILinks.Base_Socket_Streaming_Url.getLink() + String.valueOf(channelId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("STREAMS ", response.toString());

                        // check have ids
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    // make call
                                    String callerId = jsonObject.getString("id");

                                    if (!callerId.equalsIgnoreCase(callId))
                                        callDelegate.makeCallTO(callerId);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else
                            callDelegate.onReadyToCall(callId);


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("STREAMS ERROR ", anError.getMessage());
                        Log.d("STREAMS ERROR ", anError.getErrorBody());
                        Log.d("STREAMS ERROR ", anError.getErrorDetail());
                    }
                });

    }

    public static void SearchForUsers(String content, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Search_User_Url.getLink() + "?name=" + content + "&take=10&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Search Result ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("users")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("users"));
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

                        Log.d("Search Result  ", anError.getMessage());
                        Log.d("Search Result  ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void SendMessageToUser(String message, int userIdTo, ConnectionDelegate connectionDelegate) {

        Log.d("Send Message To user ID", String.valueOf(userIdTo));
        Log.d("Send Message To user  ", message);

        AndroidNetworking.post(APILinks.Message_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("user_id_to", String.valueOf(userIdTo))
                .addBodyParameter("message", message)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Send Message To user ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Send Message To user ", String.valueOf(anError.getErrorCode()));
                        Log.d("Send Message To user ", anError.getMessage());

                    }
                });

    }

    public static void GetMessagesByUser(int userId, ConnectionDelegate connectionDelegate) {

        Log.d("Messages From User ID ", String.valueOf(userId));

        AndroidNetworking.get(APILinks.Message_Url.getLink() + "?user_id=" + String.valueOf(userId) + "&take=10&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Messages From User ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("messages")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("messages"));
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

                        Log.d("Messages From User ", anError.getMessage());
                        Log.d("Messages From User ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetUsersMessages(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Message_Url.getLink() + "/users")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Messages Users ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("messages")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("messages"));
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

                        Log.d("Messages Users ", anError.getMessage());
                        Log.d("Messages Users ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetSystemMessages(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.System_Message_Url.getLink() + "?take=1&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("SYSTEM MESSAGES ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("messages")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("messages"));
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

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("SYSTEM MESSAGES ", anError.getMessage());
                        Log.d("SYSTEM MESSAGES ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetExploreContent(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Explore_Channels_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("EXPLORE ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("data")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONObject("data"));
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

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("EXPLORE ", anError.getMessage());
                        Log.d("EXPLORE ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetExploreGifts(ConnectionDelegate connectionDelegate) {

        Log.d("LINK ", APILinks.Explore_Gifts_Url.getLink());

        AndroidNetworking.get(APILinks.Explore_Gifts_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("EXPLORE GIFTS ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("gifts")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONObject("gifts"));
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

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("EXPLORE GIFTS ", anError.getMessage());
                        Log.d("EXPLORE GIFTS ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetGifts(ConnectionDelegate connectionDelegate) {

        Log.d("LINK ", APILinks.Gifts_Url.getLink());

        AndroidNetworking.get(APILinks.Gifts_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("GIFTS ", response.toString());

                        // handle parse user data
                        connectionDelegate.onConnectionSuccess(response);

                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("GIFTS ", anError.getMessage());
                        Log.d("GIFTS ", anError.getErrorDetail());
                    }
                });

    }

    public static void StoreGift(int channelId, int userIdTo, int giftId, ConnectionDelegate connectionDelegate) {

        Log.d("ROOM ", String.valueOf(channelId));

        AndroidNetworking.post(APILinks.Gifts_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("gift_id", String.valueOf(giftId))
                .addBodyParameter("user_id_to", String.valueOf(userIdTo))
                .addBodyParameter("channel_id", String.valueOf(channelId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(" Store Gift", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Store Gift ", anError.getMessage());
                        Log.d("Store Gift ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }


    public static void SetUserAttendance(UserAttendanceType attendanceType, int channelId, ConnectionDelegate connectionDelegate) {

        Log.d("ROOM ", String.valueOf(channelId));

        AndroidNetworking.post(APILinks.Channels_Url.getLink() + "/" + channelId + "/attendance")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("type", attendanceType.name().toLowerCase())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Set Attendance ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Set Attendance ", anError.getMessage());
                        Log.d("Set Attendance ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetUserAttendance(int channelId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/" + channelId + "/attendance")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Get Attendance ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Get Attendance ", anError.getMessage());
                        Log.d("Get Attendance ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateUserImage(String imageName, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Base_User_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("image", imageName)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change Image", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("user")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("user"));
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

                        Log.d("Change Image ", anError.getMessage());
                        Log.d("Change Image ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateUserName(String name, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Base_User_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("name", name)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change Name", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("user")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("user"));
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

                        Log.d("Change Name ", anError.getMessage());
                        Log.d("Change Name ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateUserBirthDate(String birthday, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Base_User_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("birthday", birthday)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change birthday", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("user")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("user"));
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

                        Log.d("Change birthday ", anError.getMessage());
                        Log.d("Change birthday ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void UpdateUserGender(String gender, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.put(APILinks.Base_User_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("gender", gender)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Change gender", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            if (response.has("user")) {
                                try {
                                    connectionDelegate.onConnectionSuccess(response.getJSONArray("user"));
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

                        Log.d("Change gender ", anError.getMessage());
                        Log.d("Change gender ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetFollowers(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/followers?take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Followers ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Followers ", anError.getMessage());
                        Log.d("Followers ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void GetFollowing(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/followings?take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Following ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Following ", anError.getMessage());
                        Log.d("Following ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void GetVisitors(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_API_Url.getLink() + "visit/" + userId)

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Following ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response.optJSONArray("visitors"));
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Following ", anError.getMessage());
                        Log.d("Following ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void GetProfileGifts(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/gifts?take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Profile Gifts ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Profile Gifts  ", anError.getMessage());
                        Log.d("Profile Gifts  ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void GetScoreUsers(int channelId, ConnectionDelegate connectionDelegate) {

        Log.d("CHANNEL_ID ", String.valueOf(channelId));

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/" + String.valueOf(channelId) + "/top?take=10")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("User Scores ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("User Scores ", anError.getMessage());
                        Log.d("User Scores ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetUserDetails(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/" + String.valueOf(userId))

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("User Details ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("User Details ", anError.getMessage());
                        Log.d("User Details ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetProfileReceivedRooms(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/" + String.valueOf(userId) + "/channels?take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ProfileReceivedRooms ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("ProfileReceivedRooms ", anError.getMessage());
                        Log.d("ProfileReceivedRooms ", anError.getErrorDetail());

                    }
                });
    }

    public static void GetProfileReceivedGifts(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/" + String.valueOf(userId) + "/gifts?take=50&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ProfileReceivedRooms ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("ProfileReceivedRooms ", anError.getMessage());
                        Log.d("ProfileReceivedRooms ", anError.getErrorDetail());

                    }
                });
    }

    public static void GetProfilePosts(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Featured_Posts_Url.getLink() + "?user_id=" + userId + "&take=50&skip=0")

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

    public static void GetEarnGoldInfo(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Earn_Gold_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Earn Gold ", response.toString());

                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Earn Gold ", anError.getMessage());
                        Log.d("Earn Gold ", anError.getErrorDetail());
                    }
                });
    }

    public static void GetMicUsers(int channelId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/" + String.valueOf(channelId) + "/speaker")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Mic Users ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Mic Users ", anError.getMessage());
                        Log.d("Mic Users ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void SetMicForUser(int channelId, int micId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(APILinks.Channels_Url.getLink() + "/" + String.valueOf(channelId) + "/speaker")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("mic", String.valueOf(micId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Set Mic ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Set Mic ", anError.getMessage());
                        Log.d("Set Mic ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });

    }

    public static void GetChannelsByCountry(String country, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/country/" + country + "?skip=0&take=50")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("CHANNELS ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("CHANNELS ", anError.getMessage());
                        Log.d("CHANNELS ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetChannelsByTag(String tag, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/tag/" + tag + "?skip=0&take=50")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("CHANNELS ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("CHANNELS ", anError.getMessage());
                        Log.d("CHANNELS ", anError.getErrorDetail());
                    }
                });

    }

    public static void GetProfileBadges(int userId, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Base_User_Url.getLink() + "/" + String.valueOf(userId) + "/badges?take=500&skip=0")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Profile Badges ", response.toString());

                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Profile Badges ", anError.getMessage());
                        Log.d("Profile Badges ", anError.getErrorDetail());
                    }
                });
    }

    public static void GetMyBadges(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.My_Profile_Badges_Url.getLink())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Profile Badges ", response.toString());

                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Profile Badges ", anError.getMessage());
                        Log.d("Profile Badges ", anError.getErrorDetail());
                    }
                });
    }

    public static void GetAllBadges(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.All_Badges_Url.getLink())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Profile Badges ", response.toString());

                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response.optJSONArray("badges"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Profile Badges ", anError.getMessage());
                        Log.d("Profile Badges ", anError.getErrorDetail());
                    }
                });
    }

    public static void GetAdvertisement(ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Advertisement_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("Advertisement ", response.toString());

                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Advertisement ", anError.getMessage());
                        Log.d("Advertisement ", anError.getErrorDetail());
                    }
                });
    }

    public static void SendFollowToUser(int userId, ConnectionDelegate connectionDelegate) {

        Log.d("Follow To user ID", String.valueOf(userId));

        AndroidNetworking.post(APILinks.Follow_Url.getLink())

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("user_id", String.valueOf(userId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Follow To user  ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null) {
                            connectionDelegate.onConnectionSuccess(response);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);

                        Log.d("Follow To user  ", String.valueOf(anError.getErrorCode()));
                        Log.d("Follow To user  ", anError.getMessage());

                    }
                });

    }

    public static void GetRoomInfo(int id, ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(APILinks.Channels_Url.getLink() + "/" + id + "/details")

                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Followers ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Followers ", anError.getMessage());
                        Log.d("Followers ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void FollowRoom(int roomId, ConnectionDelegate connectionDelegate) {
        AndroidNetworking.post(APILinks.Follow_Channel_Url.getLink())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .addBodyParameter("channel_id", String.valueOf(roomId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Followers ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Followers ", anError.getMessage());
                        Log.d("Followers ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }


    public static void GetRoomTopGiftUser(int roomId, ConnectionDelegate connectionDelegate) {
        AndroidNetworking.get(APILinks.Base_API_Url.getLink() + "channel/" + roomId + "/top")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", APIUtils.getAuthorization())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Followers ", response.toString());

                        // handle parse user data
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionSuccess(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Followers ", anError.getMessage());
                        Log.d("Followers ", anError.getErrorDetail());

                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }
}
