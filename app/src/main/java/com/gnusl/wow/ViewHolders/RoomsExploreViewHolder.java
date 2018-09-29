package com.gnusl.wow.ViewHolders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Managers.CenterLayoutManager;
import com.gnusl.wow.Decorators.SpacesItemDecoration;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.GraphicsUtil;

/**
 * Created by Yehia on 9/29/2018.
 */

public class RoomsExploreViewHolder extends SectionedViewHolder {

    private RecyclerView roomsRecyclerView;

    public RoomsExploreViewHolder(View itemView) {
        super(itemView);

        roomsRecyclerView = (RecyclerView) itemView.findViewById(R.id.rooms_recycler_view);
    }

    public void onBind(Context context, ExploreSection section) {

        CenterLayoutManager centerLayoutManager= new CenterLayoutManager(context);
        centerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        roomsRecyclerView.setLayoutManager(centerLayoutManager);

        roomsRecyclerView.addItemDecoration(new SpacesItemDecoration(GraphicsUtil.pxFromDp(6)));

        RoomsRecyclerViewAdapter roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(context, section.getRooms());
        roomsRecyclerViewAdapter.setExplore(true);
        roomsRecyclerView.setAdapter(roomsRecyclerViewAdapter);

        // scrool to center item
        roomsRecyclerView.smoothScrollToPosition(1);

    }

}