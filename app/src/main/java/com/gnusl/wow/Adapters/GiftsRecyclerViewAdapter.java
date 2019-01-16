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

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.GiftsActivity;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class GiftsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Gift> gifts;
    private int selectedPosition=-1;

    public GiftsRecyclerViewAdapter(Context context, ArrayList<Gift> gifts) {
        this.context = context;
        this.gifts = gifts;
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

        private View root_view;
        private AppCompatImageView giftImage;
        private TextView price;

        public GiftViewHolder(View itemView) {
            super(itemView);

            root_view=itemView.findViewById(R.id.root_view);
            giftImage= itemView.findViewById(R.id.gift_image);
            price= itemView.findViewById(R.id.price);
        }

        public void bind(final Gift gift, final int position) {

            // price
            price.setText(String.valueOf(gift.getPrice()));

            // image
            if (gift.getPath() != null && !gift.getPath().isEmpty())
                Glide.with(context)
                        .load(gift.getPath())
                        .into(giftImage);

            // selected item
            if(position==selectedPosition)
                root_view.setBackground(context.getResources().getDrawable(R.drawable.gift_background_selected_item));
            else
                root_view.setBackground(context.getResources().getDrawable(R.drawable.gift_background_unselected_item));

            itemView.setOnClickListener(v->{

                selectedPosition=position;
                notifyDataSetChanged();
            });
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

