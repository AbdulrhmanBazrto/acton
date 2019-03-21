package com.gnusl.wow.Application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.facebook.FacebookSdk;
import com.gnusl.wow.Managers.FontManager;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.core.Twitter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Yehia on 9/21/2018.
 */

public class WowApplication extends Application {

    private static WowApplication applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        // initialize Font Manager
        FontManager.init(getAssets());

        // initialize firebase
        FirebaseApp.initializeApp(this);

        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        // twitter
        Twitter.initialize(this);

        // initialize Android networking
        AndroidNetworking.initialize(getApplicationContext(),new OkHttpClient().newBuilder().connectTimeout(6,TimeUnit.SECONDS).build());


//        String ln  = SharedPreferencesUtils.getLanguage();
//        Log.d("LANGUAGE in application",ln);
//        Locale locale = new Locale(ln);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
////        LocaleHelper.setLocale(this ,ln);
//        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public static WowApplication getApplicationInstance() {
        return applicationInstance;
    }

    public static Context getAppContext() {
        return getApplicationInstance().getApplicationContext();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }
}
