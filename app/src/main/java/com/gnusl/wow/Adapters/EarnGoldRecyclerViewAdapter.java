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
        return new EarnGoldViewHolder (view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((EarnGoldViewHolder ) holder).bind(getEarnGoldTasks().get(position), position);

    }

    @Override
    public int getItemCount() {
        return earnGoldTasks.size();
    }

    public class EarnGoldViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView content;
        private AppCompatImageView room_image;
        private AppCompatImageView flag_image;
        private TextView user_number;


        public EarnGoldViewHolder (View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            user_number = itemView.findViewById(R.id.user_number);
            room_image = itemView.findViewById(R.id.room_image);
            flag_image = itemView.findViewById(R.id.flag_image);

        }

        public void bind(final EarnGoldTask earnGoldTask, final int position) {
            

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


