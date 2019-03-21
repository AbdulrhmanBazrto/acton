package com.gnusl.wow.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.gnusl.wow.Application.WowApplication;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ln  = SharedPreferencesUtils.getLanguage(this);
        Log.d("LANGUAGE in splash",ln);
//        Resources res = WowApplication.getApplicationInstance().getApplicationContext().getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        conf.setLocale(new Locale(ln.toLowerCase()));
//
//        res.updateConfiguration(conf, dm);

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
