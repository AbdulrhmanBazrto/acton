package com.gnusl.wow.SlidingPopUp;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;

public abstract class SwipeBackActivity
        extends AppCompatActivity
        implements SwipeBackViewGroup.SwipeBackListener {

    private static final SwipeBackViewGroup.DragEdge DEFAULT_DRAG_EDGE = SwipeBackViewGroup.DragEdge.TOP;

    private SwipeBackViewGroup swipeBackViewGroup;
    private ImageView shadowView;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getContainer());
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        swipeBackViewGroup.addView(view);
    }

    private View getContainer() {

        RelativeLayout container = new RelativeLayout(this);

        swipeBackViewGroup = new SwipeBackViewGroup(this);
        swipeBackViewGroup.setDragEdge(DEFAULT_DRAG_EDGE);
        swipeBackViewGroup.setOnSwipeBackListener(this);

        shadowView = new ImageView(this);
        shadowView.setBackgroundColor(ContextCompat.getColor(this, R.color.shadow));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(shadowView, params);
        container.addView(swipeBackViewGroup);

        return container;
    }

    public void setDragEdge(SwipeBackViewGroup.DragEdge dragEdge) {
        swipeBackViewGroup.setDragEdge(dragEdge);
    }

    int lastFractionScreen = -1;
    int lastMovedFractionScreen = -1;

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {

        shadowView.setAlpha(1 - fractionScreen);

        if (lastFractionScreen == -1) {
            float floatValue = Math.abs(fractionScreen - 1);
            floatValue *= 24;
            int value = Math.round(floatValue);
            lastFractionScreen = value;
            lastMovedFractionScreen = value;

        }

        float floatValue = Math.abs(fractionScreen - 1);
        floatValue *= 24;
        int newFractionScreen = Math.round(floatValue);
        Log.i("newFractionScreen",newFractionScreen+"");

        if (newFractionScreen != lastFractionScreen || newFractionScreen == 0) {

            lastFractionScreen = newFractionScreen;

            if (Math.abs(lastMovedFractionScreen - lastFractionScreen) > 2 || newFractionScreen == 0) {
                lastMovedFractionScreen = lastFractionScreen;
               // EventBus.getDefault().post(new PopupActivityMessageEvent(newFractionScreen, false));
            }

        }
    }




}
