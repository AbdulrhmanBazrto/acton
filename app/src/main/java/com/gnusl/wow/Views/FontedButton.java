package com.gnusl.wow.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.gnusl.wow.Managers.FontManager;
import com.gnusl.wow.R;

/**
 * Created by Yehia on 9/11/2018.
 */

public class FontedButton extends android.support.v7.widget.AppCompatButton {

    public FontedButton(Context context) {
        super(context);
    }

    public FontedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontedView);

        if (ta != null) {

            String fontAsset = new String();
            int style = Typeface.NORMAL;

            for (int i = 0; i < ta.length(); ++i) {
                final int attr = ta.getIndex(i);
                if (attr == R.styleable.FontedView_typefaceAsset)
                    fontAsset = ta.getString(attr);
            }


            if (fontAsset != null && !(fontAsset.isEmpty())) {

                Typeface tf = FontManager.getInstance().getFont(fontAsset);

                if (getTypeface() != null) {
                    style = getTypeface().getStyle();
                }

                if (tf != null) {
                    setTypeface(tf, style);
                }
            }

            ta.recycle();

        }


    }

    public void setEnabled(boolean enabled, boolean alpha) {
        if (enabled)
            this.animate().alpha(1f).setDuration(250).start();
        else if (alpha)
            this.animate().alpha(0.6f).setDuration(250).start();
        super.setEnabled(enabled);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.animate().alpha(1f).setDuration(250).start();
        super.setEnabled(enabled);
    }
}