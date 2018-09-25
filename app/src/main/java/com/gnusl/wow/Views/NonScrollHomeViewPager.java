package com.gnusl.wow.Views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Yehia on 9/23/2018.
 */

public class NonScrollHomeViewPager extends ViewPager {

    private boolean isPagingEnabled = false;

    public NonScrollHomeViewPager (Context context) {
        super(context);
    }

    public NonScrollHomeViewPager (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

}
