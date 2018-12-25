package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Delegates.SelectLockTypeDelegate;
import com.gnusl.wow.Delegates.SelectRoomTypeDelegate;
import com.gnusl.wow.Models.RoomLockType;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class RoomLockTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<RoomLockType> roomTypes;
    private SelectLockTypeDelegate selectLockTypeDelegate;

    public RoomLockTypeAdapter(Context context, ArrayList<RoomLockType> roomTypes, SelectLockTypeDelegate selectLockTypeDelegate) {
        this.context = context;
        this.roomTypes = roomTypes;
        this.selectLockTypeDelegate=selectLockTypeDelegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.room_lock_type_view_holder, parent, false);
        return new RoomTypeLockViewHolder (view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((RoomTypeLockViewHolder)holder).bind(getRoomTypes().get(position), position);

    }

    @Override
    public int getItemCount() {
        return roomTypes.size();
    }

    public class RoomTypeLockViewHolder extends RecyclerView.ViewHolder {

        private TextView month_text;
        private TextView price_text;

        public RoomTypeLockViewHolder  (View itemView) {
            super(itemView);

            month_text=(TextView) itemView.findViewById(R.id.month_text);
            price_text=(TextView) itemView.findViewById(R.id.price_text);

        }

        public void bind(final RoomLockType roomType, final int position) {

            // months
            month_text.setText(roomType.getName());

            // call delegate
            price_text.setText(String.valueOf(roomType.getDuration()));

            // clicl item
            itemView.setOnClickListener(v->selectLockTypeDelegate.onSelectRoomLockType(roomType));
        }

    }

    // region setters and getters

    public ArrayList<RoomLockType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ArrayList<RoomLockType> roomTypes) {
        this.roomTypes = roomTypes;
    }


    // endregion
}


