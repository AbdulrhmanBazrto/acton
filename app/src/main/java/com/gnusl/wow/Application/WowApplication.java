package com.gnusl.wow.Application;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.gnusl.wow.Managers.FontManager;
import com.google.firebase.FirebaseApp;
import com.twitter.sdk.android.core.Twitter;

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
    }

    public static WowApplication getApplicationInstance() {
        return applicationInstance;
    }

    public static Context getAppContext() {
        return getApplicationInstance().getApplicationContext();
    }

}
