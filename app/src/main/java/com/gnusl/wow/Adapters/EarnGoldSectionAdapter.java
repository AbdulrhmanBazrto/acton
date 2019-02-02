package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Models.EarnGoldSection;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;
import com.gnusl.wow.ViewHolders.CountriesExploreViewHolder;
import com.gnusl.wow.ViewHolders.EarnGoldBlockViewHolder;
import com.gnusl.wow.ViewHolders.EarnGoldHeaderViewHolder;
import com.gnusl.wow.ViewHolders.ExploreHeaderViewHolder;
import com.gnusl.wow.ViewHolders.RoomsExploreViewHolder;
import com.gnusl.wow.ViewHolders.TopGiftsExploreViewHolder;

import java.util.ArrayList;

public class EarnGoldSectionAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder> {

    private Context context;
    private ArrayList<EarnGoldSection> earnGoldSections;

    public EarnGoldSectionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getSectionCount() {
        return getEarnGoldSections().size();
    }

    @Override
    public int getItemCount(int section) {
        return getEarnGoldSections().get(section).getEarnGoldTasks().size();
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) { // Date Header Section

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earn_gold_header_view_holder, parent, false);
            return new EarnGoldHeaderViewHolder(v);

        } else{

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earn_gold_block_view_holder, parent, false);
            return new EarnGoldBlockViewHolder(v);
        }

    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {

        EarnGoldSection earnGoldSection=getEarnGoldSections().get(section);
        ((EarnGoldHeaderViewHolder) holder).onBind(earnGoldSection);
    }

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {

        EarnGoldSection earnGoldSection= getEarnGoldSections().get(section);
        ((EarnGoldBlockViewHolder) holder).onBind(context,earnGoldSection);
    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {
    }

    public ArrayList<EarnGoldSection> getEarnGoldSections() {
        return earnGoldSections;
    }

    public void setEarnGoldSections(ArrayList<EarnGoldSection> earnGoldSections) {
        this.earnGoldSections = earnGoldSections;
    }
}

