package com.gnusl.wow.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
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
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.R;
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;

public class BadgesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Badge> badges;

    public BadgesRecyclerViewAdapter(Context context, ArrayList<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.badge_view_holder, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((BadgeViewHolder) holder).bind(getBadges().get(position), position);

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {

        private View root_view;
        private AppCompatImageView badgeImage;
        private TextView price;
        private ProgressBar progressBar;

        public BadgeViewHolder(View itemView) {
            super(itemView);

            root_view = itemView.findViewById(R.id.root_view);
            badgeImage = itemView.findViewById(R.id.badge_image);
            progressBar = itemView.findViewById(R.id.load_progress);
            price = itemView.findViewById(R.id.price);
        }

        public void bind(final Badge gift, final int position) {

            badgeImage.setVisibility(View.INVISIBLE);
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

                                badgeImage.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                progressBar.setVisibility(View.INVISIBLE);
                                badgeImage.setVisibility(View.VISIBLE);

                                return false;
                            }
                        })
                        .into(badgeImage);

        }

    }

    // region setters and getters

    public ArrayList<Badge> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<Badge> badges) {
        this.badges = badges;
    }


    // endregion
}

