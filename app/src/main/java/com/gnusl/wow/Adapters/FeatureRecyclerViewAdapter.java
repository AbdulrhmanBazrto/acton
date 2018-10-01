package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 10/1/2018.
 */

public class FeatureRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<FeaturePost> featurePosts;

    public FeatureRecyclerViewAdapter(Context context, ArrayList<FeaturePost> featurePosts) {
        this.context = context;
        this.featurePosts = featurePosts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.feature_post_view_holder, parent, false);
        return new FeaturePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((FeaturePostViewHolder) holder).bind(getFeaturePosts().get(position), position);

    }

    @Override
    public int getItemCount() {
        return featurePosts.size();
    }

    public class FeaturePostViewHolder extends RecyclerView.ViewHolder {

        public FeaturePostViewHolder(View itemView) {
            super(itemView);

        }

        public void bind(final FeaturePost featurePost, final int position) {

        }

    }

    // region setters and getters

    public ArrayList<FeaturePost> getFeaturePosts() {
        return featurePosts;
    }

    public void setFeaturePosts(ArrayList<FeaturePost> featurePosts) {
        this.featurePosts = featurePosts;
    }


    // endregion
}

