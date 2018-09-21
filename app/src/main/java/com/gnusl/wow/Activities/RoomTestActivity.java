package com.gnusl.wow.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;

public class RoomTestActivity extends AppCompatActivity {

    private JitsiMeetView view;
    private final static String HOST="fat7al.com";
    private final static String PROTOCOL="https";
    private final static int PORT=7443;
    private final static String FILE_NAME="room/test";

    @Override
    public void onBackPressed() {
        if (!JitsiMeetView.onBackPressed()) {
            // Invoke the default handler if it wasn't handled by React.
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_room_test);

        view = new JitsiMeetView(this);
        try {
            view.loadURL(new URL(PROTOCOL,HOST,PORT,FILE_NAME));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        view.dispose();
        view = null;

        JitsiMeetView.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        JitsiMeetView.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        JitsiMeetView.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        JitsiMeetView.onHostPause(this);
    }

}
