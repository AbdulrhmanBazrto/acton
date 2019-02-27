package com.gnusl.wow.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gnusl.wow.Fragments.MyMomentsFragment;
import com.gnusl.wow.Fragments.RoomLockFragment;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

public class RoomLockSettingActivity extends AppCompatActivity {

    public final static String CHANNEL_KEY = "channel_key";
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_lock_setting);

        if (getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));
        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RoomLockFragment roomLockFragment = RoomLockFragment.newInstance();
        fragmentTransaction.replace(R.id.room_lock_frame_layout, roomLockFragment).commit();

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
