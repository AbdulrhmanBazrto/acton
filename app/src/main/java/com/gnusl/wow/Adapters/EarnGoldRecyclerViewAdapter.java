package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Models.EarnGoldTask;
import com.gnusl.wow.Models.EarnGoldTask;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class EarnGoldRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<EarnGoldTask> earnGoldTasks;

    public EarnGoldRecyclerViewAdapter(Context context, ArrayList<EarnGoldTask> earnGoldTasks) {
        this.context = context;
        this.earnGoldTasks = earnGoldTasks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.earn_gold_task_view_holder, parent, false);
        return new EarnGoldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((EarnGoldViewHolder) holder).bind(getEarnGoldTasks().get(position), position);

    }

    @Override
    public int getItemCount() {
        return earnGoldTasks.size();
    }

    public class EarnGoldViewHolder extends RecyclerView.ViewHolder {

        private ImageView taskIcon;
        private TextView taskDescription;
        private TextView price;
        private TextView status;

        public EarnGoldViewHolder(View itemView) {
            super(itemView);

            taskIcon = itemView.findViewById(R.id.task_icon);
            taskDescription = itemView.findViewById(R.id.task_description);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status_button);

        }

        public void bind(final EarnGoldTask earnGoldTask, final int position) {

            // title
            if (earnGoldTask.getTaskDescription() != null && !earnGoldTask.getTaskDescription().isEmpty())
                taskDescription.setText(earnGoldTask.getTaskDescription());

            // price
            price.setText(String.valueOf(earnGoldTask.getPrice()));

            // icon
            if (earnGoldTask.getIconUrl() != null && !earnGoldTask.getIconUrl().isEmpty())
                Glide.with(context)
                        .load(earnGoldTask.getIconUrl())
                        .into(taskIcon);

            // status
            if(earnGoldTask.isStatusReady()){

                status.setText("استلام");
                status.setTextColor(context.getResources().getColor(R.color.white));
                status.setBackgroundResource(R.drawable.background_primary_inside_shape);
            }else {
                status.setText("التالي");
                status.setTextColor(context.getResources().getColor(R.color.active_color_dark));
                status.setBackgroundResource(R.drawable.background_transparent_inside_with_primary_stroke_shape);
            }

        }

    }

    // region setters and getters

    public ArrayList<EarnGoldTask> getEarnGoldTasks() {
        return earnGoldTasks;
    }

    public void setEarnGoldTasks(ArrayList<EarnGoldTask> earnGoldTasks) {
        this.earnGoldTasks = earnGoldTasks;
    }


    // endregion
}


