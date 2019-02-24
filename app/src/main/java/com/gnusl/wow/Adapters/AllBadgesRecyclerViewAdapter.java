package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class AllBadgesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Badge> badges;

    public AllBadgesRecyclerViewAdapter(Context context, ArrayList<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.badge1_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(badges.get(position), position);

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView badgeImage;


        public UserViewHolder(View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badge_image);

        }

        public void bind(final Badge badge, final int position) {

            if (badge.isGranted()) {
                if (badge.getPath() != null && !badge.getPath().isEmpty())
                    Glide.with(context)
                            .load(badge.getPath())
                            .into(badgeImage);
            } else {
                if (badge.getPath() != null && !badge.getPath().isEmpty())
                    Glide.with(context)
                            .load(badge.getPath_gray())
                            .into(badgeImage);
            }

        }

    }

    public void setUsers(ArrayList<Badge> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }

}

