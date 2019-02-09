package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Activities.RoomByCountryActivity;
import com.gnusl.wow.Activities.RoomByTagActivity;
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

        ((ActivityViewHolder) holder).bind(getActivities().get(position),position);

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout rootView;
        private TextView activityTextView;

        public ActivityViewHolder(View itemView) {
            super(itemView);

            activityTextView= itemView.findViewById(R.id.activity_text);
            rootView= itemView.findViewById(R.id.root_view);
        }

        public void bind(final ActivitiesHashTag activity,int position) {

            // content
            activityTextView.setText(String.valueOf("#"+activity.getActivity()));

            // go to room filter
            itemView.setOnClickListener(v->{

                RoomByTagActivity.show(context,activity.getActivity());
            });

            // coloring
            if(position %3==0){ // gray

                rootView.setBackgroundResource(R.drawable.activity_backround_label_gray_shape);

            }else if(position %3==1){
                rootView.setBackgroundResource(R.drawable.activity_backround_label_gold_shape);

            }else {
                rootView.setBackgroundResource(R.drawable.activity_backround_label_brouwn_shape);
            }
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


