package com.gnusl.wow.ViewHolders;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Adapters.ActivityRecyclerViewAdapter;
import com.gnusl.wow.Adapters.CountryRecyclerViewAdapter;
import com.gnusl.wow.Adapters.GiftsRecyclerViewAdapter;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;

/**
 * Created by Yehia on 9/29/2018.
 */

public class CountriesExploreViewHolder extends SectionedViewHolder {

    private RecyclerView countryRecyclerView;
    private RecyclerView activityRecyclerView;

    public CountriesExploreViewHolder(View itemView) {
        super(itemView);

        countryRecyclerView = (RecyclerView) itemView.findViewById(R.id.countries_recycler_view);
        activityRecyclerView = (RecyclerView) itemView.findViewById(R.id.activities_recycler_view);
    }

    public void onBind(Context context, ExploreSection section) {

        // countries

        countryRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        CountryRecyclerViewAdapter countryRecyclerViewAdapter = new CountryRecyclerViewAdapter(context, section.getCountries());
        countryRecyclerView.setAdapter(countryRecyclerViewAdapter);

        // activities
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        activityRecyclerView.setLayoutManager(linearLayoutManager);

        ActivityRecyclerViewAdapter activityRecyclerViewAdapter= new ActivityRecyclerViewAdapter(context, section.getActivities());
        activityRecyclerView.setAdapter(activityRecyclerViewAdapter);
    }

}