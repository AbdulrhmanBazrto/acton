package com.gnusl.wow.Delegates;

import android.support.v4.app.Fragment;

/**
 * Created by Yehia on 9/23/2018.
 */

public interface PagerDelegate {

    void onReplaceFragment(Fragment fragment, int position);
}

