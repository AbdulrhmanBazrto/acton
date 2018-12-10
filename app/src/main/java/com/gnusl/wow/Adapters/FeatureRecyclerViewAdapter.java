package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.CommentsPostActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Connection.APILinks;
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

        private AppCompatImageView post_icon;
        private AppCompatImageView post_image;
        private AppCompatImageView message_icon;
        private TextView text_content;
        private TextView post_title;
        private TextView message_number;
        private TextView like_number;

        private SparkButton like;

        public FeaturePostViewHolder(View itemView) {
            super(itemView);

            post_icon = (AppCompatImageView) itemView.findViewById(R.id.post_icon);
            post_image = (AppCompatImageView) itemView.findViewById(R.id.post_image);
            message_icon = (AppCompatImageView) itemView.findViewById(R.id.message_icon);
            text_content = (TextView) itemView.findViewById(R.id.text_content);
            post_title = (TextView) itemView.findViewById(R.id.post_title);
            message_number = (TextView) itemView.findViewById(R.id.message_number);
            like_number = (TextView) itemView.findViewById(R.id.like_number);
            like = (SparkButton) itemView.findViewById(R.id.like_icon);
        }

        public void bind(final FeaturePost post, final int position) {

            // content
            if (post.getDescription() != null && !post.getDescription().isEmpty())
                text_content.setText(post.getDescription());

            // post image
            if (post.getImage() != null && !post.getImage().isEmpty())
                Glide.with(context)
                        .load(APILinks.Base_Media_Url.getLink()+post.getImage())
                        .into(post_image);

            // user name
            if (post.getUser() != null && post.getUser().getName()!=null && !post.getUser().getName().isEmpty())
                post_title.setText(post.getUser().getName());

            // user image
            if (post.getUser() != null && post.getUser().getImage_url()!=null && !post.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(post.getUser().getImage_url())
                        .into(post_icon);

            // like status
            handleLikeStatus(post);

            // likes numbers
            like_number.setText(String.valueOf(post.getNumLikes()));

            // comments numbers
            message_number.setText(String.valueOf(post.getNumComments()));

            // go to comments activity
            message_icon.setOnClickListener(v->goToCommentActivity(post.getId()));
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

        private void goToCommentActivity(int postId) {

            Intent intent = new Intent(context, CommentsPostActivity.class);
            intent.putExtra(CommentsPostActivity.POST_ID, postId);
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

