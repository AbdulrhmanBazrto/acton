package com.gnusl.wow.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.gnusl.wow.Managers.FontManager;
import com.gnusl.wow.R;

/**
 * Created by Yehia on 9/11/2018.
 */

public class FontedTextView extends android.support.v7.widget.AppCompatTextView {

    public FontedTextView(Context context) {
        this(context, null);
    }

    public FontedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public FontedTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontedView);

        if (ta != null) {

            String fontAsset = new String();
            int style = Typeface.NORMAL;
            float size = getTextSize();

            for (int i = 0; i < ta.length(); ++i) {
                final int attr = ta.getIndex(i);
                if (attr == R.styleable.FontedView_typefaceAsset)
                    fontAsset = ta.getString(attr);
            }

            ta.recycle();

            if (fontAsset != null && !(fontAsset.isEmpty())) {

                Typeface tf = FontManager.getInstance().getFont(fontAsset);

                if (getTypeface() != null) {
                    style = getTypeface().getStyle();
                }

                if (tf != null) {
                    setTypeface(tf, style);
                } else
                    Log.d("FontTextView", String.format("Could not create a font from asset: %s", fontAsset));
            }
        }
    }


}