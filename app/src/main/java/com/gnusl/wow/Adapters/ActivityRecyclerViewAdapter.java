package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Models.ActivitiesHashTag;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class ActivityRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ActivitiesHashTag> activities;

    public ActivityRecyclerViewAdapter(Context context, ArrayList<ActivitiesHashTag> activities) {
        this.context = context;
        this.activities=activities;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.activity_view_holder, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ActivityViewHolder) holder).bind(getActivities().get(position));

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView activityTextView;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            activityTextView=(TextView)itemView.findViewById(R.id.activity_text);
        }

        public void bind(final ActivitiesHashTag activity) {

            // content
            activityTextView.setText(String.valueOf("#"+activity.getActivity()));

        }

    }

    // region setters and getters

    public ArrayList<ActivitiesHashTag> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivitiesHashTag> activities) {
        this.activities = activities;
    }


    // endregion
}


