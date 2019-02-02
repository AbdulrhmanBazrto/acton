package com.gnusl.wow.ViewHolders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Adapters.EarnGoldRecyclerViewAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Decorators.SpacesItemDecoration;
import com.gnusl.wow.Managers.CenterLayoutManager;
import com.gnusl.wow.Models.EarnGoldSection;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.GraphicsUtil;

public class EarnGoldBlockViewHolder extends SectionedViewHolder {

    private RecyclerView earnGoldTasksRecyclerView;

    public EarnGoldBlockViewHolder(View itemView) {
        super(itemView);

        earnGoldTasksRecyclerView = itemView.findViewById(R.id.earn_gold_tasks_recycler_view);
    }

    public void onBind(Context context, EarnGoldSection earnGoldSection) {

        CenterLayoutManager centerLayoutManager= new CenterLayoutManager(context);
        centerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        earnGoldTasksRecyclerView.setLayoutManager(centerLayoutManager);

        EarnGoldRecyclerViewAdapter earnGoldRecyclerViewAdapter= new EarnGoldRecyclerViewAdapter(context, earnGoldSection.getEarnGoldTasks());
        earnGoldTasksRecyclerView.setAdapter(earnGoldRecyclerViewAdapter);

    }

}