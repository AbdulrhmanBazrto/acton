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

public class FontedEditText extends android.support.v7.widget.AppCompatEditText {

    private Context context;
    private AttributeSet attrs;
    private int defStyle;

    public FontedEditText(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public FontedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        init();
    }

    public FontedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.defStyle = defStyleAttr;
        init();
    }

    private void init() {

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
                }
            }

        }

    }
}