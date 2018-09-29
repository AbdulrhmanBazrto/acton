package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class GiftsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Gift> gifts;

    public GiftsRecyclerViewAdapter(Context context, ArrayList<Gift> gifts) {
        this.context = context;
        this.gifts=gifts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.gift_view_holder, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((GiftViewHolder) holder).bind(getGifts().get(position), position);

    }

    @Override
    public int getItemCount() {
        return gifts.size();
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

        public void bind(final Gift gift, final int position) {

            // backround image
            backroundImageView.setImageResource(gift.getBackroundResource());

            // gift image
            giftImage.setImageResource(gift.getImageResource());

            // content
            giftContentTextView.setText(gift.getContent());

            // handle people adapter
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            peopleRecyclerView.setLayoutManager(linearLayoutManager);

            PeopleRecyclerViewAdapter peopleRecyclerViewAdapter= new PeopleRecyclerViewAdapter(context,gift.getPeople());
            peopleRecyclerView.setAdapter(peopleRecyclerViewAdapter);

        }

    }

    // region setters and getters

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }


    // endregion
}

