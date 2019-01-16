package com.gnusl.wow.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MediaGridViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SoftInputDelegate;
import com.gnusl.wow.Fragments.RoomChatFragment;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.KeyboardUtils;
import com.gnusl.wow.WebRtcClient.CallDelegate;
import com.gnusl.wow.WebRtcClient.PeerConnectionParameters;
import com.gnusl.wow.WebRtcClient.PermissionChecker;
import com.gnusl.wow.WebRtcClient.WebRtcClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;

public class RoomChatActivity extends AppCompatActivity implements WebRtcClient.RtcListener, CallDelegate, SoftInputDelegate, ConnectionDelegate {

    public final static String CHANNEL_KEY = "channel_key";
    private Room room;

    private final static int VIDEO_CALL_SENT = 666;
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
    private GLSurfaceView vsv;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;
    private WebRtcClient client;
    private String mSocketAddress;
    private String callerId;

    private static final String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    protected PermissionChecker permissionChecker = new PermissionChecker();
    BroadcastReceiver imageBroadcastReceiver;
    private RoomChatFragment roomChatFragment;

    private View softInputKeyboardLayout;
    private EditText messageEditText;
    private ProgressDialog progressDialog;
    private boolean isUploadingForBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        if (getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));

        // WebRtc implementation
        AudioConferenceWebRtcImplementation();

        // inflate room chat fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        roomChatFragment = RoomChatFragment.newInstance();
        fragmentTransaction.replace(R.id.frame_container, roomChatFragment).commit();

        // initialize views
        initViews();

        // init media
        InitializeMediaPicker();
    }

    private void initViews() {

        softInputKeyboardLayout = findViewById(R.id.input_layout);
        messageEditText = findViewById(R.id.message_edit_text);

        findViewById(R.id.send_button).setOnClickListener(v -> SendMessageRequest());

        // set data
        // room image
        if (room.getBackgroundUrl() != null && !room.getBackgroundUrl().isEmpty())
            Glide.with(this)
                    .load(room.getBackgroundUrl())
                    .into(((AppCompatImageView) findViewById(R.id.backround_image)));
    }

    private void AudioConferenceWebRtcImplementation() {

        getWindow().addFlags(
                // WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        //| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mSocketAddress = "http://" + getResources().getString(R.string.host);
        mSocketAddress += (":" + getResources().getString(R.string.port) + "/");

       /* vsv = (GLSurfaceView) findViewById(R.id.glview_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

        // local and remote render
        remoteRender = VideoRendererGui.create(
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);*/

        // Initialize WebRtc Client
        initializeWebRtcClient();

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            callerId = segments.get(0);
        }
        checkPermissions();
    }


    private void checkPermissions() {
        permissionChecker.verifyPermissions(this, RequiredPermissions, new PermissionChecker.VerifyPermissionsCallback() {

            @Override
            public void onPermissionAllGranted() {

            }

            @Override
            public void onPermissionDeny(String[] permissions) {
                Toast.makeText(RoomChatActivity.this, "Please grant required permissions.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeWebRtcClient() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                false, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);

        //setting speakerphone on
        AudioManager audioManager = ((AudioManager) getSystemService(AUDIO_SERVICE));
        @SuppressWarnings("deprecation")
        boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
        audioManager.setMode(isWiredHeadsetOn ? AudioManager.MODE_IN_CALL : AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(!isWiredHeadsetOn);

        client = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext(), getRoom().getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        //vsv.onPause();
        if (client != null) {
            client.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //vsv.onResume();
        if (client != null) {
            client.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (client != null) {
            client.onDestroy();
        }
        unregisterReceiver(imageBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onCallReady(String callId) {

        // start streaming on server
        if (PermissionChecker.hasPermissions(this, RequiredPermissions)) {
            client.start("Audio_Conference_android_test");
        }

        // send request to know how much clients to make call with them
        APIConnectionNetwork.GetStreamClients(getRoom().getId(), callId, this);

    }

    public void answer(String callerId) throws JSONException {
        client.sendMessage(callerId, "init", null);
        startCam();
    }

    public void call(String callId) {
        Intent msg = new Intent(Intent.ACTION_SEND);
        msg.putExtra(Intent.EXTRA_TEXT, mSocketAddress + callId);
        msg.setType("text/plain");
        startActivityForResult(Intent.createChooser(msg, "Call someone :"), VIDEO_CALL_SENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CALL_SENT) {
            startCam();
        }
    }

    public void startCam() {
        // Camera settings
        if (PermissionChecker.hasPermissions(this, RequiredPermissions)) {
            client.start("Audio_Conference_android_test");
        }
    }

    @Override
    public void onStatusChanged(final String newStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), newStatus, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocalStream(MediaStream localStream) {
        // localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
        /*VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);*/
    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        // remoteStream.videoTracks.get(0).addRenderer(new VideoRenderer(remoteRender));
       /* VideoRendererGui.update(remoteRender,
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                scalingType, false);*/
    }

    @Override
    public void onRemoveRemoteStream(int endPoint) {
       /* VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);*/
    }

    private void InitializeMediaPicker() {

      /*  videoBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(RoomChatActivity.this, "yippiee Video ", Toast.LENGTH_SHORT).show();
                Toast.makeText(RoomChatActivity.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                //setAdapter(intent.getStringArrayListExtra("list"));
            }
        };*/


        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(RoomChatActivity.this, "yippiee Image ", Toast.LENGTH_SHORT).show();
                Toast.makeText(RoomChatActivity.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();

                // change room backround
                handleMediaReceiver(intent.getStringArrayListExtra("list"));

            }
        };


        //  IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        //registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        MediaChooser.showCameraVideoView(false);

    }

    public void openGallery(boolean isUploadingForBackground) {

        this.isUploadingForBackground = isUploadingForBackground;

        if (checkReadGalleryPermissions(this)) {

            HomeScreenMediaChooser.startMediaChooser(this, true);

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSIONS_REQUEST);

        }

    }

    private void handleMediaReceiver(List<String> filePathList) {

        // create file image
        File mediaFile = new File(filePathList.get(0));

        if (!(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp"))) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inSampleSize = 2;
            Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);

            if (isUploadingForBackground)
                ((AppCompatImageView) findViewById(R.id.backround_image)).setImageBitmap(myBitmap);
        }

        // TODO : upload image
        // make progress dialog
        if (isUploadingForBackground)
            this.progressDialog = ProgressDialog.show(this, "", "change your background..");

        else
            this.progressDialog = ProgressDialog.show(this, "", "upload your photo..");

        // upload image
        APIConnectionNetwork.UploadImage(mediaFile, this);

    }

    private void SendMessageRequest() {

        if (messageEditText.getText().toString().isEmpty())
            Toast.makeText(this, "you must have message", Toast.LENGTH_SHORT).show();

        else {

            // send message
            APIConnectionNetwork.SendMessageByChannel(messageEditText.getText().toString(), getRoom().getId(), this);

            // share on pubnup
            roomChatFragment.ShareMessageOnPubnub(messageEditText.getText().toString());

            // clear message
            messageEditText.setText("");
        }

    }

    public void SendImageMessageRequest(String image) {

        // send image message
        APIConnectionNetwork.SendMessageByChannel(image, getRoom().getId(), this);

        // share on pubnup
        roomChatFragment.ShareMessageOnPubnub(image);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void makeCallTO(String callerId) {

        if (callerId != null) {
            try {
                answer(callerId);
                Log.d("onCallReady callerId ", callerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReadyToCall(String callId) {

        Log.d("onCallReady callId ", callId);
        // call(callId);

        // start streaming on server
        if (PermissionChecker.hasPermissions(this, RequiredPermissions)) {
            client.start("Audio_Conference_android_test");
        }
    }

    @Override
    public void onRequestToHideKeyboard() {

        softInputKeyboardLayout.setVisibility(View.GONE);

        // hide keyboard
        KeyboardUtils.hideSoftKeyboard(this);
    }

    @Override
    public void onRequestToShowKeyboard() {

        softInputKeyboardLayout.setVisibility(View.VISIBLE);
        messageEditText.requestFocus();

        // show keyboard
        KeyboardUtils.showSoftKeyboardForEditText(this, messageEditText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_PERMISSIONS_REQUEST) {


            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                if (grantResults.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        //Coming from profile
                        HomeScreenMediaChooser.startMediaChooser(this, true);

                    } else {
                        Toast.makeText(this, R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                    }

                }

            }

        } else
            permissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, "failure response..", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "error Connection try again", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        if (jsonObject.has("image")) {

            // upload post
            try {

                if (isUploadingForBackground) // change room background
                    APIConnectionNetwork.ChangeRoomBackground(jsonObject.getString("image"), getRoom().getId(), this);

                else   // send image as message
                    SendImageMessageRequest(APILinks.Base_Media_Url.getLink()+jsonObject.getString("image"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;
        }

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

}
