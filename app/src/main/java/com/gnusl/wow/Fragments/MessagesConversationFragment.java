package com.gnusl.wow.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Adapters.MessagesConversationRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.MessageImageDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.MicUser;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.NetworkUtils;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.gnusl.wow.Views.FontedEditText;
import com.gnusl.wow.Views.FontedTextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.CALENDAR_PERMISSION;
import static com.gnusl.wow.Utils.PermissionsUtils.CAMERA_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkCameraPermissions;

public class MessagesConversationFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener, MessageImageDelegate {

    private View inflatedView;
    private RecyclerView recyclerView;
    private MessagesConversationRecyclerViewAdapter messagesConversationAdapter;
    LinearLayoutManager layoutManager;
    private RelativeLayout toolbar_layout;
    private FontedEditText message_edit_text;
    private FontedTextView state_label;
    private AppCompatImageView send_button, photo_button, bigImage;

    private int user_id;

    private PubNub pubnub;
    String channelName = "awesomeChannel";
    private User toUser;

    @SuppressLint("ValidFragment")
    public MessagesConversationFragment(int user_id) {

        this.user_id = user_id;

        if (user_id > SharedPreferencesUtils.getUser().getId()) {
            channelName = "Channel_" + SharedPreferencesUtils.getUser().getId() + "_" + user_id;
        } else {
            channelName = "Channel_" + user_id + "_" + SharedPreferencesUtils.getUser().getId();
        }
    }

    public static MessagesConversationFragment newInstance(int user_id) {

        return new MessagesConversationFragment(user_id);
    }


    public MessagesConversationFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_messages_conversation_layout, container, false);

        recyclerView = inflatedView.findViewById(R.id.recycler_view_messeges_conversation);
        message_edit_text = inflatedView.findViewById(R.id.message_edit_text);
        send_button = inflatedView.findViewById(R.id.send_button);
        state_label = inflatedView.findViewById(R.id.state_label);
        // photo_button = inflatedView.findViewById(R.id.photo_button);
        bigImage = inflatedView.findViewById(R.id.bigImage);

        // init adapter
        setUpRecyclerView();

        // send message
        send_button.setOnClickListener(v -> handleClickSendMessage());

        // photo_button.setOnClickListener(v -> handleClickSendPhoto());

        sendMessagesRequest();

        APIConnectionNetwork.GetUserDetails(user_id, new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

            }

            @Override
            public void onConnectionError(ANError anError) {

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                if (jsonObject.has("user")) {

                    try {
                        toUser = User.newInstance(jsonObject.getJSONObject("user"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });

        PubnubImplementation();

        return inflatedView;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (NetworkUtils.getInstance(getContext()).isOnline()) {
            state_label.setVisibility(View.GONE);

        } else {
            state_label.setVisibility(View.VISIBLE);
        }

        messagesConversationAdapter = new MessagesConversationRecyclerViewAdapter(getContext(), new ArrayList<>(), this);
        messagesConversationAdapter.setDelegate(this);

        recyclerView.setAdapter(messagesConversationAdapter);

    }

    private void sendMessagesRequest() {

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetMessagesByUser(user_id, 0, this);
    }


    private void handleClickSendMessage() {

        if (!message_edit_text.getText().toString().isEmpty()) {
            APIConnectionNetwork.SendMessageToUser(message_edit_text.getText().toString(), user_id, this);
            ShareMessageOnPubnub(message_edit_text.getText().toString());
            message_edit_text.setText("");

        }

    }

    @Override
    public void onConnectionFailure() {

        LoaderPopUp.dismissLoader();


    }

    @Override
    public void onConnectionError(ANError anError) {

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(String response) {

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // sync messages
        LoaderPopUp.dismissLoader();

        // parsing
        ArrayList<Message> messages = Message.parseJSONArray(jsonArray);
        Collections.reverse(messages);

        if (messagesConversationAdapter.getItemCount() == 0)
            messagesConversationAdapter.setMessages(messages);
        else
            messagesConversationAdapter.addMessages(messages);

        messagesConversationAdapter.notifyDataSetChanged();
        if (messages.size() != 0)
            recyclerView.smoothScrollToPosition(messagesConversationAdapter.getItemCount() - 1);

    }

    @Override
    public void onLoadMore() {
        APIConnectionNetwork.GetMessagesByUser(user_id, messagesConversationAdapter.getItemCount(), this);
    }


    public void ShareMessageOnPubnub(String message) {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        User currentUser = SharedPreferencesUtils.getUser();

        messageJsonObject.addProperty("user_id_from", currentUser.getId());
        messageJsonObject.addProperty("user_id_to", toUser.getId());
        messageJsonObject.addProperty("msg_type", "text");
        messageJsonObject.addProperty("msg", message);
        messageJsonObject.addProperty("date", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));


        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {


            }
        });

    }

    private void PubnubImplementation() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-aa20a392-451f-11e9-8dbe-225b5c64e997");
        pnConfiguration.setPublishKey("pub-c-a723de90-86f2-4612-b578-af68a7037dba");

        this.pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                       /* pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // MessageSection successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });*/
                    }
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {


                Message chatMessage = new Message();
                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                chatMessage.setMessage(msg);

                if (message.getMessage().getAsJsonObject().get("user_id_from").getAsInt() == SharedPreferencesUtils.getUser().getId()) {
                    chatMessage.setUserTo(toUser);
                    chatMessage.setUser_id_to(toUser.getId());
                    chatMessage.setUserFrom(SharedPreferencesUtils.getUser());
                    chatMessage.setUser_id_from(SharedPreferencesUtils.getUser().getId());

                } else {

                    chatMessage.setUserFrom(toUser);
                    chatMessage.setUser_id_from(toUser.getId());
                    chatMessage.setUserTo(SharedPreferencesUtils.getUser());
                    chatMessage.setUser_id_to(SharedPreferencesUtils.getUser().getId());


                }

                chatMessage.setCreated_at(message.getMessage().getAsJsonObject().get("date").getAsString());

                getActivity().runOnUiThread(() -> {
                    messagesConversationAdapter.getMessages().add(chatMessage);
                    messagesConversationAdapter.notifyDataSetChanged();

                    // smooth scroll
                    recyclerView.smoothScrollToPosition(messagesConversationAdapter.getMessages().size() - 1);
                });


            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
        List<String> channels = new ArrayList<>();
        channels.add(channelName);
        pubnub.subscribe().channels(channels).execute();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* if (resultCode == RESULT_OK)
            switch (requestCode) {

                case PICK_IMAGE_REQUEST_CODE:
                    Log.d("profilePicture", "onActivityResult: PICK_IMAGE_PROFILE_REQUEST_CODE..");
                    Uri galleryPictureUri = data.getData();
                    Log.d("profilePicture", "onActivityResult: uri: " + galleryPictureUri.toString());

                    Log.d("profilePicture", "onActivityResult: delegate isn't null");
                    startCrop(getActivity(), galleryPictureUri);

                    break;

                case CAMERA_REQUEST_CODE:
                    Log.d("profilePicture", "onActivityResult: CAMERA_PROFILE_REQUEST_CODE..");

                    Uri cameraPictureUri = MediaUtils.photoURI; //when you put extra to intent the response data is null and you should use the same URI you have passed

                    Log.d("profilePicture", "onActivityResult: profileActionsDelegate not null, uri is: " + cameraPictureUri);
                    startCrop(getActivity(), cameraPictureUri);

                    break;

                case UCrop.REQUEST_CROP:
                    Log.d("profilePicture", "onActivityResult: REQUEST_CROP started..");

                    final Uri resultUri = UCrop.getOutput(data);
                    Log.d("profilePicture", "onActivityResult: uri: " + resultUri);

                    try {

                        Bitmap imageBitmap = SiliCompressor.with(getActivity()).getCompressBitmap(resultUri.toString());
                        Log.d("profilePicture", "onActivityResult: bitmap filled..");
                        ConnectionAPINetwork.sendImageMessage(ImageUtils.ToBase64ImageString(imageBitmap), user_id, this);

                    } catch (IOException e) {

                        Log.d("profilePicture", "onActivityResult: exception thrown " + e.toString());
                        e.printStackTrace();

                    }

                    // get bitmap
//                    selected_image=MediaUtils.userTakeImage(null,resultUri);

                    Log.d("requestCode", "onActivityResult: there is an error resultCode is: " + resultCode);
                    break;

            }
        else {
            Log.d("requestCode", "onActivityResult: there is an error resultCode is: " + resultCode);
        }*/

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != CALENDAR_PERMISSION) {


            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                if (grantResults.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && !permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR)) {

                        //Coming from profile
                        showProfilePictureChooserDialog(getActivity());

                    } else {
                        Toast.makeText(getActivity(), R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                    }

                } else if (grantResults.length == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && !permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR)) {

                        showProfilePictureChooserDialog(getActivity());

                    } else if (!permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR))
                        Toast.makeText(getActivity(), R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }


    public static final int PICK_IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static Uri photoURI;

    private void openGallery() {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE);
    }

    private void openCamera() {

       /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating file for camera photo
        File photoFile = null;
        try {
            photoFile = ImageUtils.createCameraTempPhotoFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null &&
                takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            photoURI = FileProvider.getUriForFile(getActivity(),
                    //"ai.medicus.android.fileprovider",
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }*/
    }

    public void startCrop(Activity activity, Uri uri) {

       /* Log.d("profilePicture", "startCrop started..");

        Uri outputUri;
        outputUri = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));

        UCrop uCrop = UCrop.of(uri, outputUri);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimary));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(activity, R.color.white));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, true);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "Crop image");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimary));
        }
        uCrop.start(getActivity(), this, UCrop.REQUEST_CROP);*/
    }

    public void showProfilePictureChooserDialog(Activity activity) {
       /* final String[] items = new String[]{activity.getString(R.string.from_gallery), activity.getString(R.string.from_camera)};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.camera_or_gallery_dialog_item, items);

        new AlertDialog.Builder(activity).setTitle(R.string.select_source)
                .setAdapter(adapter, (dialog, item) -> {

                    if (item == 0) {
                        openGallery();
                    } else if (item == 1) {
                        openCamera();
                    }

                })
                .show();*/
    }


    @Override
    public void openImages(Bitmap image) {
       /* if (bigImage.getVisibility() == View.GONE){
            bigImage.setVisibility(View.VISIBLE);
            bigImage.setImageBitmap(image);
            }*/
    }

   /* @Subscribe
    public void onReceiveMessageEvent(ReceiveMessageEvent receiveMessageEvent) {
        if (receiveMessageEvent.getMessage() != null) {
            Log.i("MessageSection Received ", receiveMessageEvent.getMessage().getMessage());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    buffOffset++;
                    List<Message> mm = new ArrayList<>();
                    mm.add(receiveMessageEvent.getMessage());
                    messagesConversationAdapter.update(mm);
                    try {
                        recyclerView.smoothScrollToPosition(messagesConversationAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        // EventBus.getDefault().unregister(this);
    }

}
