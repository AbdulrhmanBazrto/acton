package com.gnusl.wow.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gnusl.wow.Fragments.MyMomentsFragment;
import com.gnusl.wow.Fragments.StoreRoomLockFragment;
import com.gnusl.wow.R;

public class StoreLockRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_room);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StoreRoomLockFragment myMomentsFragment = StoreRoomLockFragment.newInstance();
        fragmentTransaction.replace(R.id.lock_room_frame_layout, myMomentsFragment).commit();

    }

}
