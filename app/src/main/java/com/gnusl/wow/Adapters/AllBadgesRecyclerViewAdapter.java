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
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllBadgesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Badge> badges;

    public AllBadgesRecyclerViewAdapter(Context context, ArrayList<Badge> badges) {
        this.context = context;
        this.badges = badges;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.badge1_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(badges.get(position), position);

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView badgeImage;
        private ProgressBar load_progress;


        public UserViewHolder(View itemView) {
            super(itemView);

            badgeImage = itemView.findViewById(R.id.badge_image);
            load_progress = itemView.findViewById(R.id.load_progress);

        }

        public void bind(final Badge badge, final int position) {

            if (badge.isGranted()) {
                if (badge.getPath() != null && !badge.getPath().isEmpty())
                    Picasso.with(context)
                            .load(badge.getPath())
                            .into(badgeImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    load_progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
            } else {
                if (badge.getPath() != null && !badge.getPath().isEmpty())
                    Picasso.with(context)
                            .load(badge.getPath_gray())
                            .into(badgeImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    load_progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (badge.isGranted()) {

                    } else {
                        APIConnectionNetwork.StoreBadge(badge.getId(), new ConnectionDelegate() {
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
                                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onConnectionSuccess(JSONArray jsonArray) {

                            }
                        });
                    }
                }
            });

        }

    }

    public void setUsers(ArrayList<Badge> badges) {
        this.badges = badges;
        notifyDataSetChanged();
    }

}

