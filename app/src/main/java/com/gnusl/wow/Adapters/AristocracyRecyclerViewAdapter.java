package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gnusl.wow.Models.AristocracyDetails;
import com.gnusl.wow.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AristocracyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<AristocracyDetails> aristocracyDetails;

    public AristocracyRecyclerViewAdapter(Context context, ArrayList<AristocracyDetails> aristocracyDetails) {
        this.context = context;
        this.aristocracyDetails = aristocracyDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.aristocracy_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(aristocracyDetails.get(position), position);

    }

    @Override
    public int getItemCount() {
        return aristocracyDetails.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView aristocracyImage;
        private ProgressBar load_progress;
        private TextView aristocracyDesc;


        public UserViewHolder(View itemView) {
            super(itemView);

            aristocracyImage = itemView.findViewById(R.id.aristocracy_image);
            aristocracyDesc = itemView.findViewById(R.id.aristocracy_desc);
            load_progress = itemView.findViewById(R.id.load_progress);

        }

        public void bind(final AristocracyDetails aristocracyDetails, final int position) {

            if (aristocracyDetails.isValid()) {
                if (aristocracyDetails.getImage() != null && !aristocracyDetails.getImage().isEmpty())
                    Picasso.with(context)
                            .load(aristocracyDetails.getImage())
                            .into(aristocracyImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    load_progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
            } else {
                if (aristocracyDetails.getImage() != null && !aristocracyDetails.getImage().isEmpty())
                    Picasso.with(context)
                            .load(aristocracyDetails.getImageGray())
                            .into(aristocracyImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    load_progress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
            }

            aristocracyDesc.setText(aristocracyDetails.getDescription());

        }

    }

    public void setAristocracies(List<AristocracyDetails> aristocracies) {
        this.aristocracyDetails = aristocracies;
        notifyDataSetChanged();
    }

}

