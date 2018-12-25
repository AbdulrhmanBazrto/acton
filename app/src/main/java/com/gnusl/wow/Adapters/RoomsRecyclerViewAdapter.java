package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.RoomChatActivity;
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
    private boolean isExplore = false;

    public RoomsRecyclerViewAdapter(Context context, ArrayList<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
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

        private TextView title;
        private TextView content;
        private AppCompatImageView room_image;
        private AppCompatImageView flag_image;
        private TextView user_number;


        public RoomViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            user_number = (TextView) itemView.findViewById(R.id.user_number);
            room_image = (AppCompatImageView) itemView.findViewById(R.id.room_image);
            flag_image = (AppCompatImageView) itemView.findViewById(R.id.flag_image);

        }

        public void bind(final Room room, final int position) {

            if (isExplore) {  // handle dynamic width item in explore fragment

                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                double devicewidth = displaymetrics.widthPixels / 1.3;

                //if you need 4-5-6 anything fix imageview in height
                int deviceheight = displaymetrics.heightPixels / 4;

                itemView.getLayoutParams().width = (int) devicewidth;

            }

            if (room != null) {

                // title
                if (room.getName() != null && !room.getName().isEmpty())
                    title.setText(room.getName());

                // content
                if (room.getDescription() != null && !room.getDescription().isEmpty())
                    content.setText(room.getDescription());

                // room image
                if (room.getBackgroundUrl() != null && !room.getBackgroundUrl().isEmpty())
                    Glide.with(context)
                            .load(room.getBackgroundUrl())
                            .into(room_image);


                // flag image
                if (room.getCountryCodeUrl() != null && !room.getCountryCodeUrl().isEmpty())
                    Glide.with(context)
                            .load(room.getCountryCodeUrl())
                            .into(flag_image);

                // go to Chat Room
                itemView.setOnClickListener(v -> {

                    goToRoomChannel(room);
                });

            }
        }

        private void goToRoomChannel(Room room){

            Intent intent=new Intent(context, RoomChatActivity.class);
            intent.putExtra(RoomChatActivity.CHANNEL_KEY,room);
            context.startActivity(intent);
        }
    }

    // region setters and getters


    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isExplore() {
        return isExplore;
    }

    public void setExplore(boolean explore) {
        isExplore = explore;
    }

    // endregion
}

