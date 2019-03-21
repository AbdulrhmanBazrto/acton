package com.gnusl.wow.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gnusl.wow.Fragments.MyMomentsFragment;
import com.gnusl.wow.Fragments.RoomTopGiftsFragment;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;

public class RoomTopGiftsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_top_gifts);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RoomTopGiftsFragment roomTopGiftsFragment = RoomTopGiftsFragment.newInstance(getIntent().getIntExtra("roomId", 0));
        fragmentTransaction.replace(R.id.room_top_gifts_frame_layout, roomTopGiftsFragment).commit();

    }

}
