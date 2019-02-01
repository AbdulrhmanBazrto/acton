package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gnusl.wow.Activities.GiftsActivity;
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.R;
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;

public class GiftsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Gift> gifts;
    private GiftDelegate giftDelegate;
    BottomSheetDialog optionsDialog;
    private int selectedPosition = -1;

    public GiftsRecyclerViewAdapter(Context context, ArrayList<Gift> gifts, BottomSheetDialog optionsDialog, GiftDelegate giftDelegate) {
        this.context = context;
        this.gifts = gifts;
        this.giftDelegate = giftDelegate;
        this.optionsDialog = optionsDialog;
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
        private ProgressBar progressBar;

        public GiftViewHolder(View itemView) {
            super(itemView);

            root_view = itemView.findViewById(R.id.root_view);
            giftImage = itemView.findViewById(R.id.gift_image);
            progressBar = itemView.findViewById(R.id.load_progress);
            price = itemView.findViewById(R.id.price);
        }

        public void bind(final Gift gift, final int position) {

            giftImage.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            // price
            price.setText(String.valueOf(gift.getPrice()));

            // image
            if (gift.getPath() != null && !gift.getPath().isEmpty())
                Glide.with(context)
                        .load(gift.getPath())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                giftImage.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.INVISIBLE);
                                giftImage.setVisibility(View.VISIBLE);

                                return false;
                            }
                        })
                        .into(giftImage);

            // selected item
            if (position == selectedPosition)
                root_view.setBackground(context.getResources().getDrawable(R.drawable.gift_background_selected_item));
            else
                root_view.setBackground(context.getResources().getDrawable(R.drawable.gift_background_unselected_item));

            itemView.setOnClickListener(v -> {

                selectedPosition = position;
                notifyDataSetChanged();

                // dismiss dialog
                if (optionsDialog != null)
                    optionsDialog.dismiss();

                // send
                if (giftDelegate != null)
                    giftDelegate.onClickToSendGift(gift);
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

