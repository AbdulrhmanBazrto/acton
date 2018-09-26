package com.gnusl.wow.Views;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Yehia on 9/23/2018.
 */

public class CustomRoomTopBar extends ConstraintLayout {

    public CustomRoomTopBar(Context context) {
        super(context);
        init();
    }

    public CustomRoomTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRoomTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        // inflate view
        LayoutInflater.from(getContext()).inflate(R.layout.custom_room_top_bar, CustomRoomTopBar.this);


    }

}
