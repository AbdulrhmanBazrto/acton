package com.gnusl.wow.Utils;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ZigZagAnimation extends Animation {
    private PathMeasure pm;
    float[] pos = new float[2];

    public ZigZagAnimation() {
        Path p = new Path();
        p.moveTo(0f, 0f);
        p.lineTo(12f, -5f);
        p.lineTo(12f, -50f);
        p.lineTo(12f, -75f);
        p.lineTo(10f, -150f);
        p.lineTo(10f, -200f);
        p.lineTo(10f, -250f);
        p.lineTo(25f, -300f);
        p.lineTo(25f, -350f);
        p.lineTo(25f, -400f);
        p.lineTo(10f, -450f);
        p.lineTo(10f, -5000f);
//        p.lineTo(13f, -550f);
//        p.lineTo(18f, -600f);
//        p.lineTo(16f, -650f);
//        p.lineTo(16f, -700f);
//        p.lineTo(15f, -750f);
//        p.lineTo(10f, -800f);
        pm = new PathMeasure(p, false);
        setDuration(5000);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float distance = pm.getLength() * interpolatedTime;
        pm.getPosTan(distance, pos, null);
        t.getMatrix().postTranslate(pos[0], pos[1]);
    }
}