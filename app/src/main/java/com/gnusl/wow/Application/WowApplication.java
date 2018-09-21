package com.gnusl.wow.Application;

import android.app.Application;

import com.gnusl.wow.Managers.FontManager;

/**
 * Created by Yehia on 9/21/2018.
 */

public class WowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize Font Manager
        FontManager.init(getAssets());
    }
}
