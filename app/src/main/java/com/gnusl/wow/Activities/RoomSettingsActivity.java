package com.gnusl.wow.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.AccountFragment;
import com.gnusl.wow.Fragments.RoomLockFragment;
import com.gnusl.wow.Fragments.RoomSettingsFragment;
import com.gnusl.wow.Fragments.SettingsFragment;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;

public class RoomSettingsActivity extends AppCompatActivity {

    public final static String CHANNEL_KEY = "channel_key";
    private Room room;

    TextView titleTv;
    Fragment currentFragment;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_settings);



        if (getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));

        titleTv = findViewById(R.id.title);

        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // default
        makeFragment(FragmentTags.RoomSettingsFragment);

    }

    public void makeFragment(FragmentTags fragmentTags) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        switch (fragmentTags) {

            case RoomSettingsFragment:

                titleTv.setText(getResources().getString(R.string.Settings));
                currentFragment = RoomSettingsFragment.newInstance();
                break;

            case RoomLockFragment:

                titleTv.setText(getResources().getString(R.string.room_lock));
                currentFragment = RoomLockFragment.newInstance();
                break;


        }

        fragmentTransaction.replace(R.id.frame_container, currentFragment).commit();

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void onBackPressed() {

        if (currentFragment instanceof RoomSettingsFragment)
            super.onBackPressed();

        else
            makeFragment(FragmentTags.RoomSettingsFragment);

    }


}
