package com.gnusl.wow.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // go to login activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(SharedPreferencesUtils.getUser()!=null){ // logged in
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                    return;
                }
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finish();
            }
        },3000);
    }
}
