package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class RechargeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public RechargeRecyclerViewAdapter(Context context) {
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.rechrage_view_holder, parent, false);
        return new RechargeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RechargeItemViewHolder) holder).bind();

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class RechargeItemViewHolder extends RecyclerView.ViewHolder {

        public RechargeItemViewHolder(View itemView) {
            super(itemView);

        }

        public void bind() {


        }

    }

    // region setters and getters


    // endregion
}
