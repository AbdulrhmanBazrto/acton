package com.gnusl.wow.Activities;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gnusl.wow.R;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;


public class CommentsPostActivity extends SlidingActivity {

    @Override
    public void init(Bundle savedInstanceState) {

        setPrimaryColors(
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.colorPrimaryDarkMain)
        );

        setContent(R.layout.activity_comments_post);
        setHeaderContent(R.layout.post_test);

        enableFullscreen();

    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }
}
