package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.SelectRoomTypeDelegate;
import com.gnusl.wow.Models.RoomType;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class RoomTypesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<RoomType> roomTypes;
    private SelectRoomTypeDelegate selectRoomTypeDelegate;

    public RoomTypesRecyclerViewAdapter(Context context, ArrayList<RoomType> roomTypes, SelectRoomTypeDelegate selectRoomTypeDelegate) {
        this.context = context;
        this.roomTypes = roomTypes;
        this.selectRoomTypeDelegate=selectRoomTypeDelegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.room_type_view_holder, parent, false);
        return new RoomTypeViewHolder (view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RoomTypeViewHolder)holder).bind(getRoomTypes().get(position), position);

    }

    @Override
    public int getItemCount() {
        return roomTypes.size();
    }

    public class RoomTypeViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView roomImage;
        private TextView roomName;

        public RoomTypeViewHolder (View itemView) {
            super(itemView);

            roomImage= itemView.findViewById(R.id.room_image);
            roomName= itemView.findViewById(R.id.room_name);

        }

        public void bind(final RoomType roomType, final int position) {

            // name
            roomName.setText(roomType.getName());

            // post image
            if (roomType.getIcon_url() != null && !roomType.getIcon_url().isEmpty())
                Glide.with(context)
                        .load(roomType.getIcon_url())
                        .into(roomImage);

            // call delegate
            itemView.setOnClickListener(v->selectRoomTypeDelegate.onSelectedRoomType(roomType));

        }

    }

    // region setters and getters

    public ArrayList<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ArrayList<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }


    // endregion
}

