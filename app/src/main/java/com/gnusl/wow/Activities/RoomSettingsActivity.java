package com.gnusl.wow.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.AccountFragment;
import com.gnusl.wow.Fragments.RoomSettingsFragment;
import com.gnusl.wow.Fragments.SettingsFragment;
import com.gnusl.wow.R;

public class RoomSettingsActivity extends AppCompatActivity {

    TextView titleTv;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_settings);

        titleTv=findViewById(R.id.title);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        // default
        makeFragment(FragmentTags.RoomSettingsFragment);

    }

    public void makeFragment(FragmentTags fragmentTags){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();


        switch (fragmentTags){

            case RoomSettingsFragment:

                titleTv.setText(getResources().getString(R.string.Settings));
                currentFragment=RoomSettingsFragment.newInstance();
                break;

        }

        fragmentTransaction.replace(R.id.frame_container,currentFragment).commit();

    }

    @Override
    public void onBackPressed() {

        if(currentFragment instanceof RoomSettingsFragment)
            super.onBackPressed();

        else
            makeFragment(FragmentTags.RoomSettingsFragment);

    }
}
