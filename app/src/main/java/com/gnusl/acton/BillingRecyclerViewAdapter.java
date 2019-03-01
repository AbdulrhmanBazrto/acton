package com.gnusl.acton;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.billingclient.api.SkuDetails;
import com.gnusl.wow.Delegates.SelectHashTagDelegate;
import com.gnusl.wow.Models.ActivitiesHashTag;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehia on 9/29/2018.
 */

public class BillingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SkuDetails> activities;
    private View.OnClickListener onClickListener;

    public BillingRecyclerViewAdapter(Context context, List<SkuDetails> activities, View.OnClickListener onClickListener) {
        this.context = context;
        this.activities = activities;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.product_item, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ActivityViewHolder) holder).bind(activities.get(position), position);

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView activityTextView;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            activityTextView = itemView.findViewById(R.id.text);
        }

        public void bind(final SkuDetails activity, int position) {

            activityTextView.setText(activity.getTitle());

            // go to room filter
            itemView.setOnClickListener(onClickListener);

        }

    }

}


