package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Activities.GiftsActivity;
import com.gnusl.wow.Models.TopGiftExplorer;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class TopGiftsExplorerRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TopGiftExplorer> topGiftExplorers;

    public TopGiftsExplorerRecyclerViewAdapter(Context context, ArrayList<TopGiftExplorer> topGiftExplorers) {
        this.context = context;
        this.topGiftExplorers = topGiftExplorers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.top_gift_explorer_view_holder, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((GiftViewHolder) holder).bind(getTopGiftExplorers().get(position), position);

    }

    @Override
    public int getItemCount() {
        return topGiftExplorers.size();
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView backroundImageView;
        private AppCompatImageView giftImage;
        private TextView giftContentTextView;
        private RecyclerView peopleRecyclerView;

        public GiftViewHolder(View itemView) {
            super(itemView);

            backroundImageView=(AppCompatImageView)itemView.findViewById(R.id.backround_shape);
            giftImage=(AppCompatImageView)itemView.findViewById(R.id.gift_image);
            giftContentTextView=(TextView)itemView.findViewById(R.id.gift_content);
            peopleRecyclerView=(RecyclerView) itemView.findViewById(R.id.people_recycler_view);
        }

        public void bind(final TopGiftExplorer topGiftExplorer, final int position) {

            // backround image
            backroundImageView.setImageResource(topGiftExplorer.getBackroundResource());

            // topGiftExplorer image
            giftImage.setImageResource(topGiftExplorer.getImageResource());

            // content
            giftContentTextView.setText(topGiftExplorer.getContent());

            // handle people adapter
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            peopleRecyclerView.setLayoutManager(linearLayoutManager);

            PeopleRecyclerViewAdapter peopleRecyclerViewAdapter= new PeopleRecyclerViewAdapter(context, topGiftExplorer.getPeople());
            peopleRecyclerView.setAdapter(peopleRecyclerViewAdapter);

            itemView.setOnClickListener(v->{

                context.startActivity(new Intent(context,GiftsActivity.class));
            });
        }

    }

    // region setters and getters

    public ArrayList<TopGiftExplorer> getTopGiftExplorers() {
        return topGiftExplorers;
    }

    public void setTopGiftExplorers(ArrayList<TopGiftExplorer> topGiftExplorers) {
        this.topGiftExplorers = topGiftExplorers;
    }


    // endregion
}

