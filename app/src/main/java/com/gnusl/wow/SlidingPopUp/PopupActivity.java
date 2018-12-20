package com.gnusl.wow.SlidingPopUp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;

public abstract class PopupActivity extends SwipeBackActivity {

    View activityWindowView;
    public static boolean isOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOpened = true;

        setDragEdge(SwipeBackViewGroup.DragEdge.TOP);

        activityWindowView = getWindow().getDecorView().findViewById(android.R.id.content);

        animateEntrance();

    }

    private void animateEntrance() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.details_slide_in_up);
        animation.setDuration(150);
        activityWindowView.setAnimation(animation);
        activityWindowView.animate();
        animation.start();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
               // EventBus.getDefault().post(new PopupActivityMessageEvent(0, true));

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                didAppear();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpened = false;
    }

    public static final String TAG = "acitvityAnimation";

    int lastBackValue = -1;

    @Override
    public void onBackPressed() {
        isOpened = false;
        Log.d(TAG, "onAnimationUpdate: onback pressed from popup activity ");

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.details_slide_out_down);
        animation.setDuration(250);

        activityWindowView.clearAnimation();
        animation.setFillAfter(true);
        activityWindowView.startAnimation(animation);
        activityWindowView.animate();

        final float[] fVal = new float[1];

        ValueAnimator valueAnimator = ValueAnimator.ofInt(8, 0);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation1 -> {

            fVal[0] = (float) ((int) animation1.getAnimatedValue() * 3);

            if (lastBackValue == -1 ||
                    lastBackValue != (int) fVal[0]) {

                lastBackValue = (int) fVal[0];
               // EventBus.getDefault().post(new PopupActivityMessageEvent(lastBackValue, false));

            }
        });

        valueAnimator.start();

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    abstract public void didAppear();

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


}
