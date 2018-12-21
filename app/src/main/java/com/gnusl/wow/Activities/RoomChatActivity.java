package com.gnusl.wow.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Delegates.SoftInputDelegate;
import com.gnusl.wow.Fragments.RoomChatFragment;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.SendMessagePopup;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.KeyboardUtils;
import com.gnusl.wow.WebRtcClient.CallDelegate;
import com.gnusl.wow.WebRtcClient.PeerConnectionParameters;
import com.gnusl.wow.WebRtcClient.PermissionChecker;
import com.gnusl.wow.WebRtcClient.WebRtcClient;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomChatActivity extends AppCompatActivity implements WebRtcClient.RtcListener, CallDelegate,SoftInputDelegate {

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

    private View softInputKeyboardLayout;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        // WebRtc implementation
        AudioConferenceWebRtcImplementation();

        // inflate room chat fragment
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, RoomChatFragment.newInstance()).commit();

        softInputKeyboardLayout=findViewById(R.id.input_layout);
        messageEditText=findViewById(R.id.message_edit_text);

    }

    private void AudioConferenceWebRtcImplementation(){

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

        client = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext());
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
        super.onDestroy();
    }

    @Override
    public void onCallReady(String callId) {

        // send request to know how much clients to make call with them
        GetStreamClients(callId,this);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionChecker.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void GetStreamClients(final String callId, final CallDelegate callDelegate) {

        AndroidNetworking.get("http://fat7al.com:3000/streams.json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("STREAMS ",response.toString());

                        // check have ids
                        if(response!=null && response.length()!=0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);

                                    // make call
                                    String callerId = jsonObject.getString("id");

                                    // if(!callerId.equalsIgnoreCase(callId))
                                    callDelegate.makeCallTO(callerId);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else
                            callDelegate.onReadyToCall(callId);


                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("ERROR ",anError.getMessage());
                    }
                });


    }

    @Override
    public void makeCallTO(String callerId) {

        if (callerId != null) {
            try {
                answer(callerId);
                Log.d("onCallReady callerId ",callerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReadyToCall(String callId) {

        Log.d("onCallReady callId ",callId);
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
        KeyboardUtils.showSoftKeyboardForEditText(this,messageEditText);
    }
}
