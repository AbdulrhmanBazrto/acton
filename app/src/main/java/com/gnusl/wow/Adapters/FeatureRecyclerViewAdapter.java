package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gnusl.wow.Activities.CommentsPostActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.R;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

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

        private SparkButton like;

        public FeaturePostViewHolder(View itemView) {
            super(itemView);

            like = (SparkButton) itemView.findViewById(R.id.like_icon);
        }

        public void bind(final FeaturePost post, final int position) {

            // like status
            handleLikeStatus(post);

            // go to comments activity
            itemView.setOnClickListener(v->goToCommentActivity());
        }

        private void handleLikeStatus(FeaturePost post){

            //like.setChecked(false);

            // like animation
            final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.blop);

            like.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {

                    // send request
                    APIConnectionNetwork.UpdateLike(post.getId(),null);

                    // play sound
                    mediaPlayer.start();
                }

                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                }

                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {

                }
            });

        }

        private void goToCommentActivity() {

            Intent intent = new Intent(context, CommentsPostActivity.class);
            //intent.putExtra(CommentsPostActivity.Post_Key, user_id);
            context.startActivity(intent);

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

