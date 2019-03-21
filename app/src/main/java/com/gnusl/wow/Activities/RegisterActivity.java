package com.gnusl.wow.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.AccountFragment;
import com.gnusl.wow.Fragments.LoginFragment;
import com.gnusl.wow.Fragments.SettingsFragment;
import com.gnusl.wow.Fragments.SignUpFragment;
import com.gnusl.wow.Fragments.SignUpOrLoginFragment;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;

public class RegisterActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        // default
        makeFragment(FragmentTags.SignUpOrLoginFragment);
    }

    public void makeFragment(FragmentTags fragmentTags){

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();


        switch (fragmentTags){

            case SignUpOrLoginFragment:

                currentFragment=SignUpOrLoginFragment.newInstance();
                break;

            case SignUpFragment:

                currentFragment=SignUpFragment.newInstance();
                break;

            case LoginFragment:

                currentFragment=LoginFragment.newInstance();
                break;


        }

        fragmentTransaction.replace(R.id.frame_container,currentFragment).commit();

    }

    @Override
    public void onBackPressed() {

        if(currentFragment instanceof SignUpOrLoginFragment)
            super.onBackPressed();

        else
            makeFragment(FragmentTags.SignUpOrLoginFragment);

    }

}
