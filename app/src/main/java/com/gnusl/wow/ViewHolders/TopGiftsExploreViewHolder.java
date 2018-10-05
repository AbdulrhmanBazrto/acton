package com.gnusl.wow.ViewHolders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.gnusl.wow.Adapters.GiftsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class TopGiftsExploreViewHolder extends SectionedViewHolder {

    private RecyclerView recyclerView;

    public TopGiftsExploreViewHolder(View itemView) {
        super(itemView);

        recyclerView = (RecyclerView) itemView.findViewById(R.id.gifts_recycler_view);
    }

    public void onBind(Context context,ExploreSection section) {


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        GiftsRecyclerViewAdapter giftsRecyclerViewAdapter= new GiftsRecyclerViewAdapter(context, section.getGifts());
        recyclerView.setAdapter(giftsRecyclerViewAdapter);

    }

}
