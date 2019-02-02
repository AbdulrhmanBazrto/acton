package com.gnusl.wow.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Models.EarnGoldSection;
import com.gnusl.wow.R;

public class EarnGoldHeaderViewHolder extends SectionedViewHolder {

    private TextView title;

    public EarnGoldHeaderViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.header_title);

    }

    public void onBind(EarnGoldSection section){

        title.setText(section.getHeaderTitle());
    }
}
