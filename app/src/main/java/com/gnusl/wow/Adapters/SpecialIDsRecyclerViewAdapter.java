package com.gnusl.wow.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
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
import com.gnusl.wow.Delegates.SpecialIDClickDelegate;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.Models.SpecialID;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialIDsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SpecialID> badges;
    private SpecialIDClickDelegate specialIDClickDelegate;

    public SpecialIDsRecyclerViewAdapter(Context context, List<SpecialID> badges, SpecialIDClickDelegate specialIDClickDelegate) {
        this.context = context;
        this.badges = badges;
        this.specialIDClickDelegate = specialIDClickDelegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.special_id_view_holder, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((BadgeViewHolder) holder).bind(getBadges().get(position), position);

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtId, price;

        public BadgeViewHolder(View itemView) {
            super(itemView);

            price = itemView.findViewById(R.id.price);
            txtId = itemView.findViewById(R.id.special_id);
        }

        public void bind(final SpecialID specialID, final int position) {

            // price
            price.setText(String.valueOf(specialID.getPrice()));
            txtId.setText(specialID.getTxtId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (specialIDClickDelegate != null)
                        specialIDClickDelegate.onClick(specialID);
                }
            });

        }

    }

    // region setters and getters

    public List<SpecialID> getBadges() {
        return badges;
    }

    public void setBadges(List<SpecialID> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }


    // endregion
}

