package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.RechargeActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Background;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThemesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Room room;
    private Context context;
    private ArrayList<Background> backgrounds;
    private Background currentBackGround = null;

    public ThemesRecyclerViewAdapter(Context context, ArrayList<Background> backgrounds, Room room) {
        this.context = context;
        this.backgrounds = backgrounds;
        this.room = room;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.themes_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(backgrounds.get(position), position);

    }

    @Override
    public int getItemCount() {
        return backgrounds.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView badgeImage, ivCurrent, ivOwned;
        private ProgressBar load_progress;


        public UserViewHolder(View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badge_image);
            load_progress = itemView.findViewById(R.id.load_progress);
            ivCurrent = itemView.findViewById(R.id.iv_current);
            ivOwned = itemView.findViewById(R.id.iv_owned);

        }

        public void bind(final Background background, final int position) {

            if (background.getPath() != null && !background.getPath().isEmpty())
                Picasso.with(context)
                        .load(background.getBackgroundUrl())
                        .into(badgeImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                load_progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

            if (background.isSold()) {
                ivOwned.setVisibility(View.VISIBLE);
                ivOwned.setImageResource(R.drawable.icon_owned);
            } else {
                ivOwned.setVisibility(View.GONE);
            }

            if (currentBackGround != null && currentBackGround.getId() == background.getId()) {
                ivCurrent.setVisibility(View.VISIBLE);
                ivCurrent.setImageResource(R.drawable.ic_right_tick);
            } else {
                ivCurrent.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    APIConnectionNetwork.ChangeRoomBackgroundId(background.getId(), room.getId(), new ConnectionDelegate() {
                        @Override
                        public void onConnectionFailure() {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(anError.getErrorBody());
                                if (jsonObject.has("payment_status")) {
                                    if (jsonObject.optString("payment_status").equalsIgnoreCase("error")) {
                                        context.startActivity(new Intent(context, RechargeActivity.class));
                                    }
                                }
                            } catch (JSONException e) {

                            }
                        }

                        @Override
                        public void onConnectionSuccess(String response) {

                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            currentBackGround = background;
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onConnectionSuccess(JSONArray jsonArray) {

                        }
                    });

                }
            });

        }

    }

    public void setUsers(ArrayList<Background> backgrounds) {
        this.backgrounds = backgrounds;
        for (int i = 0; i < backgrounds.size(); i++) {
            if (backgrounds.get(i).getId() == room.getBackgroundId())
                currentBackGround = backgrounds.get(i);
        }
        notifyDataSetChanged();
    }

}

