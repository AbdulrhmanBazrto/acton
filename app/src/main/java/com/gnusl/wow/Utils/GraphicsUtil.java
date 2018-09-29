package com.gnusl.wow.Utils;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gnusl.wow.Application.WowApplication;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yehia on 9/29/2018.
 */

public class GraphicsUtil {

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static void colorShape(ImageView imageView, String colorHex) {
        Drawable background = imageView.getBackground();

        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            try {
                shapeDrawable.getPaint().setColor(Color.parseColor(colorHex));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                shapeDrawable.getPaint().setColor(Color.WHITE);

            }
            // shapeDrawable.getPaint().setColorFilter(filter);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            try {
                gradientDrawable.setColor(Color.parseColor(colorHex));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                gradientDrawable.setColor(Color.WHITE);

            }
            // gradientDrawable.setColorFilter(filter);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            try {
                colorDrawable.setColor(Color.parseColor(colorHex));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                colorDrawable.setColor(Color.WHITE);

            }
            //colorDrawable.setColorFilter(filter);
        }
    }

    public static void colorDrawableShape(Drawable background, String colorHex) {
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(Color.parseColor(colorHex));
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(Color.parseColor(colorHex));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(Color.parseColor(colorHex));
        }
    }

    public static int pxFromDp(final float dp) {
        Context context = WowApplication.getAppContext();
        if (context != null) {
            return (int) (dp * context.getResources().getDisplayMetrics().density);
        }
        return 0;
    }

    public static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth(Activity activity) {
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return 0;
    }

    static public RotateAnimation animateDropdownArrowForExpand(final ImageView dropDownArrow) {

        RotateAnimation animation = new RotateAnimation(0f, 180f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(400);
        animation.setFillAfter(true);
        dropDownArrow.startAnimation(animation);
        return animation;

    }

    static public RotateAnimation animateProfileDropdownArrow(final ImageView dropDownArrow) {

        RotateAnimation animation = new RotateAnimation(0f, 180f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(400);
        // animation.setFillAfter(true);
        dropDownArrow.startAnimation(animation);
        return animation;

    }

    static public RotateAnimation animateDropdownArrowForCollapse(final ImageView dropDownArrow) {

        RotateAnimation animation = new RotateAnimation(180f, 360f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
        animation.setFillAfter(true);
        dropDownArrow.startAnimation(animation);
        return animation;
    }

    public static void animateLayoutColor(final View Layout, Context context, int resColorStart, int resColorEnd, int duration) {
        int colorFrom = ContextCompat.getColor(context, resColorStart);
        int colorTo = ContextCompat.getColor(context, resColorEnd);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                Layout.setBackgroundColor((int) animator.getAnimatedValue());
                Log.d("color", animator.getAnimatedValue() + "");
            }

        });
        colorAnimation.start();
    }

}
