package com.gnusl.wow.Utils;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Random;

public class ZigZagAnimation extends Animation {
    private PathMeasure pm;
    float[] pos = new float[2];

    public ZigZagAnimation(Context context) {

        Random rander = new Random();
        int Max = 4;
        int Min = 1;
        int i = rander.nextInt(Max - Min + 1);
        Log.d("postion", String.valueOf(i));
        Path p = new Path();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;

        float a = ((height / 10f) * 1);
        float b = ((height / 10f) * 2);
        float c = ((height / 10f) * 3);
        float d = ((height / 10f) * 4);
        float e = ((height / 10f) * 5);
        float f = ((height / 10f) * 6);
        float g = ((height / 10f) * 7);
        float h = ((height / 10f) * 8);

        switch (i) {
            case 1: {
                p.moveTo(0f, 0f);
                p.lineTo(12f, -a);
                p.lineTo(12f, -b);
                p.lineTo(45f, -c);
                p.lineTo(45f, -d);
                p.lineTo(10f, -e);
                p.lineTo(10f, -f);
                p.lineTo(10f, -g);
                p.lineTo(10f, -h + 100);
                break;
            }
            case 2: {
                p.moveTo(0f, 0f);
                p.lineTo(15f, -a);
                p.lineTo(15f, -b);
                p.lineTo(45f, -c);
                p.lineTo(10f, -d);
                p.lineTo(10f, -h);
                break;
            }
            case 3: {
                p.moveTo(0f, 0f);
                p.lineTo(25f, -a);
                p.lineTo(17f, -b);
                p.lineTo(13f, -c);
                p.lineTo(5f, -d);
                p.lineTo(5f, -e);
                p.lineTo(10f, -f - 100);
                break;
            }
            default: {
                p.moveTo(0f, 0f);
                p.lineTo(15f, -a);
                p.lineTo(10f, -b);
                p.lineTo(14f, -c);
                p.lineTo(18f, -d);
                p.lineTo(12f, -e);
                p.lineTo(10f, -f - 200);
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