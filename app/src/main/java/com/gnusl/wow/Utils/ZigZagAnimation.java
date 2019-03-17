package com.gnusl.wow.Utils;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import java.util.Random;

public class ZigZagAnimation extends Animation {
    private PathMeasure pm;
    float[] pos = new float[2];

    public ZigZagAnimation() {

        Random rander = new Random();
        int Max = 4;
        int Min = 1;
        int i = rander.nextInt(Max - Min + 1);
        Log.d("postion", String.valueOf(i));
        Path p = new Path();

        switch (i) {
            case 1: {
                p.moveTo(0f, 0f);
                p.lineTo(12f, -5f);
                p.lineTo(12f, -50f);
//                p.lineTo(12f, -75f);
//                p.lineTo(10f, -150f);
//                p.lineTo(10f, -200f);
//                p.lineTo(10f, -250f);
//                p.lineTo(25f, -300f);
                p.lineTo(45f, -350f);
                p.lineTo(45f, -400f);
                p.lineTo(10f, -450f);
                p.lineTo(10f, -1400f);
                break;
            }
            case 2: {
                p.moveTo(0f, 0f);
//                p.lineTo(30f, -5f);
//                p.lineTo(30f, -50f);
//                p.lineTo(30f, -75f);
//                p.lineTo(15f, -150f);
                p.lineTo(15f, -200f);
                p.lineTo(15f, -250f);
                p.lineTo(45f, -300f);
//                p.lineTo(25f, -350f);
//                p.lineTo(25f, -400f);
                p.lineTo(10f, -850f);
                p.lineTo(10f, -1100f);
                break;
            }
            case 3: {
                p.moveTo(0f, 0f);
                p.lineTo(25f, -5f);
//                p.lineTo(25f, -50f);
//                p.lineTo(25f, -75f);
//                p.lineTo(17f, -150f);
                p.lineTo(17f, -200f);
                p.lineTo(13f, -250f);
//                p.lineTo(28f, -300f);
//                p.lineTo(28f, -350f);
//                p.lineTo(28f, -400f);
                p.lineTo(5f, -650f);
                p.lineTo(5f, -1200f);
                break;
            }
            default: {
                p.moveTo(0f, 0f);
                p.lineTo(15f, -5f);
                p.lineTo(10f, -25f);
                p.lineTo(14f, -85f);
                p.lineTo(18f, -150f);
                p.lineTo(12f, -200f);
                p.lineTo(10f, -250f);
                p.lineTo(27f, -300f);
                p.lineTo(29f, -370f);
                p.lineTo(27f, -400f);
                p.lineTo(15f, -750f);
                p.lineTo(15f, -1500f);
                break;
            }
        }
        pm = new PathMeasure(p, false);
        setDuration(2000);


    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float distance = pm.getLength() * interpolatedTime;
        pm.getPosTan(distance, pos, null);
        t.getMatrix().postTranslate(pos[0], pos[1]);
    }
}