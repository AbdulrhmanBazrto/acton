package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gnusl.wow.Activities.CommentsPostActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Delegates.PostActionsDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.R;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;

/**
 * Created by Yehia on 10/1/2018.
 */

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<FeaturePost> featurePosts;
    private PostActionsDelegate postActionsDelegate;
    private boolean isFollowing;
    private boolean isLoading = false;
    private OnLoadMoreListener loadMoreListener;

    private static int POST_HOLDER = 0;
    private static int LOAD_MORE_HOLDER = 1;

    public PostsRecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<FeaturePost> featurePosts, OnLoadMoreListener delegate, PostActionsDelegate postActionsDelegate, boolean isFollowing) {
        this.context = context;
        this.featurePosts = featurePosts;
        this.loadMoreListener = delegate;
        this.postActionsDelegate = postActionsDelegate;
        this.isFollowing = isFollowing;

        // setup recycler listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(-1)) {
                    Log.d("TOP ", true + "");
                } else if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    Log.d("BOTTOM ", true + "");

                    onScrollToBottomToLoadMore();
                }

            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == POST_HOLDER) {
            view = inflater.inflate(R.layout.feature_post_view_holder, parent, false);
            return new FeaturePostViewHolder(view);

        } else {
            view = inflater.inflate(R.layout.load_more_view_holder, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FeaturePostViewHolder) {

            ((FeaturePostViewHolder) holder).bind(getFeaturePosts().get(position), position);

        } else
            ((LoadingViewHolder) holder).bind();

    }

    @Override
    public int getItemCount() {
        return isLoading ? featurePosts.size() + 1 : featurePosts.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoading && position == featurePosts.size())
            return LOAD_MORE_HOLDER;
        else {
            return POST_HOLDER;
        }

    }

    private void onScrollToBottomToLoadMore() {

        setLoading(true);

        if (loadMoreListener != null)
            loadMoreListener.onLoadMore();
    }

    public class FeaturePostViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView post_icon;
        private AppCompatImageView post_image;
        private AppCompatImageView message_icon;
        private AppCompatImageView more_icon;
        private TextView text_content;
        private TextView post_title;
        private TextView message_number;
        private TextView like_number;

        private SparkButton like;

        public FeaturePostViewHolder(View itemView) {
            super(itemView);

            post_icon = itemView.findViewById(R.id.post_icon);
            post_image = itemView.findViewById(R.id.post_image);
            message_icon = itemView.findViewById(R.id.message_icon);
            more_icon = itemView.findViewById(R.id.more_icon);
            text_content = itemView.findViewById(R.id.text_content);
            post_title = itemView.findViewById(R.id.post_title);
            message_number = itemView.findViewById(R.id.message_number);
            like_number = itemView.findViewById(R.id.like_number);
            like = itemView.findViewById(R.id.like_icon);
        }

        public void bind(final FeaturePost post, final int position) {

            // content
            if (post.getDescription() != null && !post.getDescription().isEmpty())
                text_content.setText(post.getDescription());

            // post image
            if (post.getImage() != null && !post.getImage().isEmpty())
                Glide.with(context)
                        .load(APILinks.Base_Media_Url.getLink() + post.getImage())
                        .into(post_image);

            // user name
            if (post.getUser() != null && post.getUser().getName() != null && !post.getUser().getName().isEmpty())
                post_title.setText(post.getUser().getName());

            // user image
            if (post.getUser() != null && post.getUser().getImage_url() != null && !post.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(post.getUser().getImage_url())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                post_icon.setImageResource(R.drawable.ic_launcher_wow);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(post_icon);

            // like status
            handleLikeStatus(post);

            // likes numbers
            like_number.setText(String.valueOf(post.getNumLikes()));

            // comments numbers
            message_number.setText(String.valueOf(post.getNumComments()));

            // go to comments activity
            message_icon.setOnClickListener(v -> goToCommentActivity(post.getId()));

            more_icon.setOnClickListener(v -> {

                if (isFollowing)
                    return;

                PopupMenu dropDownMenu = new PopupMenu(context, more_icon);
                dropDownMenu.getMenuInflater().inflate(R.menu.comment_menu_option, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.edit_comment:

                                postActionsDelegate.onEditPost(post);
                                break;

                            case R.id.delete_comment:

                                postActionsDelegate.onDeletePost(post);
                                break;
                        }

                        return true;
                    }
                });
                dropDownMenu.show();
            });

        }

        private void handleLikeStatus(FeaturePost post) {

            like.setChecked(post.isLiked());

            // like animation
            final MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.blop);

            like.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {

                    // send request
                    APIConnectionNetwork.UpdateLike(post.getId(), null);

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

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.load_more_progress);
        }

        public void bind() {
        }
    }

    // region setters and getters

    public ArrayList<FeaturePost> getFeaturePosts() {
        return featurePosts;
    }

    public void setFeaturePosts(ArrayList<FeaturePost> featurePosts) {
        this.featurePosts = featurePosts;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    // endregion
}

