package com.gnusl.wow.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;
import com.gnusl.wow.ViewHolders.CountriesExploreViewHolder;
import com.gnusl.wow.ViewHolders.ExploreHeaderViewHolder;
import com.gnusl.wow.ViewHolders.RoomsExploreViewHolder;
import com.gnusl.wow.ViewHolders.TopGiftsExploreViewHolder;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class ExploreRecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<SectionedViewHolder>{

    private Context context;
    private Fragment fragment;
    private FragmentActivity hostActivity;
    private ArrayList<ExploreSection> exploreSections = new ArrayList<>();

    private final static int GIFT_SECTION=100;
    private final static int COUNTRIES_SECTION=200;
    private final static int ROOMS_SECTION=300;

    public ExploreRecyclerViewSectionAdapter(Context context, Fragment fragment, FragmentActivity hostActivity) {
        this.context = context;
        this.fragment = fragment;
        this.hostActivity = hostActivity;
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {

        ExploreSection exploreSection = getExploreSections().get(section);

        if(exploreSection.getGifts()!=null)
            return GIFT_SECTION;

        else if(exploreSection.getCountries()!=null && exploreSection.getActivities()!=null)
            return COUNTRIES_SECTION;

        else
            return ROOMS_SECTION;

    }


    @Override
    public int getSectionCount() {
        return getExploreSections().size();
    }

    @Override
    public int getItemCount(int section) {
        return 1;
    }

    @Override
    public SectionedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) { // Date Header Section

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explore_header_view_holder, parent, false);
            return new ExploreHeaderViewHolder(v);

        } else if (viewType == GIFT_SECTION) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explore_gifts_view_holder, parent, false);
            return new TopGiftsExploreViewHolder(v);

        } else if (viewType == COUNTRIES_SECTION) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explore_countries_view_holder, parent, false);
            return new CountriesExploreViewHolder(v);

        } else{

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.explore_rooms_view_holder, parent, false);
            return new RoomsExploreViewHolder(v);
        }


    }

    @Override
    public void onBindHeaderViewHolder(SectionedViewHolder holder, int section, boolean expanded) {

        ExploreSection exploreSection=getExploreSections().get(section);
        ((ExploreHeaderViewHolder) holder).onBind(exploreSection);
    }

    @Override
    public void onBindViewHolder(SectionedViewHolder holder, int section, int relativePosition, int absolutePosition) {

        ExploreSection exploreSection= getExploreSections().get(section);

        if (holder instanceof TopGiftsExploreViewHolder)
            ((TopGiftsExploreViewHolder) holder).onBind(context,exploreSection);

        else if (holder instanceof CountriesExploreViewHolder)

            ((CountriesExploreViewHolder) holder).onBind(exploreSection);

        else if (holder instanceof RoomsExploreViewHolder)

            ((RoomsExploreViewHolder) holder).onBind(exploreSection);

    }

    @Override
    public void onBindFooterViewHolder(SectionedViewHolder holder, int section) {
    }

    public ArrayList<ExploreSection> getExploreSections() {
        return exploreSections;
    }

    public void setExploreSections(ArrayList<ExploreSection> exploreSections) {
        this.exploreSections = exploreSections;
    }
}
