package com.gnusl.wow.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.AccountFragment;
import com.gnusl.wow.Fragments.BlockListFragment;
import com.gnusl.wow.Fragments.SettingsFragment;
import com.gnusl.wow.R;

public class SettingsActivity extends AppCompatActivity {

    TextView titleTv;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        titleTv=findViewById(R.id.title);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        // default
        makeFragment(FragmentTags.SettingsFragment);
    }

    public void makeFragment(FragmentTags fragmentTags){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();


        switch (fragmentTags){

            case SettingsFragment:

                titleTv.setText(getResources().getString(R.string.settings));
                currentFragment=SettingsFragment.newInstance();
                break;

            case AccountFragment:

                titleTv.setText(getResources().getString(R.string.account));
                currentFragment=AccountFragment.newInstance();
                break;

            case BlockListFragment:

                titleTv.setText(getResources().getString(R.string.block_list));
                currentFragment=BlockListFragment.newInstance();
                break;

        }

        fragmentTransaction.replace(R.id.frame_container,currentFragment).commit();

    }

    @Override
    public void onBackPressed() {

        if(currentFragment instanceof SettingsFragment)
            super.onBackPressed();

        else
            makeFragment(FragmentTags.SettingsFragment);

    }
}
