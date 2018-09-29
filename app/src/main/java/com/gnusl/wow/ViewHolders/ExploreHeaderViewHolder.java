package com.gnusl.wow.ViewHolders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yehia on 9/29/2018.
 */

public class ExploreHeaderViewHolder extends SectionedViewHolder {

    private TextView title;

    public ExploreHeaderViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.header_title);

    }

    public void onBind(ExploreSection section){

        title.setText(section.getHeaderTitle());
    }
}

