package com.gnusl.wow.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yehia on 9/27/2018.
 */

public class RoomsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Room> rooms;

    public RoomsRecyclerViewAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms=rooms;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.room_view_holder, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RoomViewHolder) holder).bind(getRooms().get(position), position);

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        public RoomViewHolder(View itemView) {
            super(itemView);

        }

        public void bind(final Room room, final int position) {

        }

    }

    // region setters and getters


    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    // endregion
}

