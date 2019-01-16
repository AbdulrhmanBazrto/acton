package com.gnusl.wow.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.MessageImageDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.klinker.android.sliding.TouchlessScrollView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagesConversationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messages;

    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false;
    private int updatedPosition;

    private static int MESSAGE_HOLDER_LTR = 0;
    private static int MESSAGE_HOLDER_RTL = 1;
    private static int MESSAGE_IMAGE_HOLDER_LTR = 3;
    private static int MESSAGE_IMAGR_HOLDER_RTL = 4;
    private static int LOAD_MORE_HOLDER = 2;

    MessageImageDelegate messageImageDelegate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MessagesConversationRecyclerViewAdapter(Context context, List<Message> messages, ViewGroup scrollView, OnLoadMoreListener delegate) {
        this.context = context;
        this.messages = messages;
        this.loadMoreListener = delegate;

        // setup recycler listener
        if (scrollView != null) {
            if (scrollView instanceof RecyclerView)
                ((RecyclerView) scrollView).addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (!recyclerView.canScrollVertically(-1)) {
                            Log.d("TOP ", true + "");
                            onScrollToBottomToLoadMore();
                        } else if (!isLoading && !recyclerView.canScrollVertically(1)) {
                            Log.d("BOTTOM ", true + "");

                            //  onScrollToBottomToLoadMore();
                        }

                    }
                });

            else if (scrollView instanceof NestedScrollView)
                ((NestedScrollView) scrollView).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        if (scrollY > oldScrollY) {
                            Log.i("NESTED SCROLL ", "Scroll DOWN");
                        }
                        if (scrollY < oldScrollY) {
                            Log.i("NESTED SCROLL ", "Scroll UP");
                        }

                        if (scrollY == 0) {
                            Log.i("NESTED SCROLL ", "TOP SCROLL");

                        }

                        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                            Log.i("NESTED SCROLL ", "BOTTOM SCROLL");
                            onScrollToBottomToLoadMore();
                        }
                    }
                });

            else if (scrollView instanceof TouchlessScrollView)
                scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        // We take the last son in the scrollview
                        View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

                        // if diff is zero, then the bottom has been reached
                        if (diff == 0) {
                            // do stuff
                            Log.i("SCROLL VIEW ", "BOTTOM SCROLL");
                            onScrollToBottomToLoadMore();
                        }
                    }
                });
        }

    }

    private void onScrollToBottomToLoadMore() {

        if (loadMoreListener != null)
            loadMoreListener.onLoadMore();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MESSAGE_HOLDER_LTR) {
            view = inflater.inflate(R.layout.message_conversation_view_holder_ltr, parent, false);
            return new MessegeViewHolder(view);
        } else if (viewType == MESSAGE_HOLDER_RTL) {
            view = inflater.inflate(R.layout.message_conversation_view_holder_rtl, parent, false);
            return new MessegeViewHolder(view);
        } else if (viewType == MESSAGE_IMAGR_HOLDER_RTL) {
            view = inflater.inflate(R.layout.message_image_conversation_view_holder_rtl, parent, false);
            return new MessegeImageViewHolder(view);
        } else if (viewType == MESSAGE_IMAGE_HOLDER_LTR) {
            view = inflater.inflate(R.layout.message_image_conversation_view_holder_ltr, parent, false);
            return new MessegeImageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.load_more_view_holder, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessegeViewHolder)
            ((MessegeViewHolder) holder).bind(messages.get(updatedPosition), updatedPosition);
        else if (holder instanceof MessegeImageViewHolder)

            ((MessegeImageViewHolder) holder).bind(messages.get(updatedPosition), updatedPosition);
        else
            ((LoadingViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return isLoading ? messages.size() + 1 : messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoading)
            updatedPosition = position - 1; // see with position -1 is not used for message
        else
            updatedPosition = position;

        if (isLoading && position == 0)
            return LOAD_MORE_HOLDER;

        // return position % 2 == 0 ? MESSAGE_HOLDER_LTR: MESSAGE_HOLDER_RTL;

       /* if (messages.get(updatedPosition).getMessage().contains(".png")|| messages.get(updatedPosition).getMessage().contains(".jpg"))
            return User.isFromUser(messages.get(updatedPosition)) ? MESSAGE_IMAGR_HOLDER_RTL : MESSAGE_IMAGE_HOLDER_LTR;
        else
            return User.isFromUser(messages.get(updatedPosition)) ? MESSAGE_HOLDER_RTL : MESSAGE_HOLDER_LTR;*/

        return MESSAGE_HOLDER_LTR;
    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.load_more_progress);
            progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        public void bind() {
        }
    }

    public class MessegeViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;
        private AutoFitFontedTextView msg;


        public MessegeViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            msg = itemView.findViewById(R.id.msg);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void bind(final Message message, final int position) {

            // change orintation
            //itemView.setLayoutDirection(position % 2 == 0 ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);

            // handle from user meesage
            if (message.getUserFrom() != null) {

                User user = message.getUserFrom();

                // name
                user_name.setText(user.getName());

                // image
                if (user.getImage_url() != null && !user.getImage_url().equalsIgnoreCase("null"))
                    Glide.with(context)
                            .load(user.getImage_url())
                            .into(profile_image);

            }

            // comment msg
            msg.setText(message.getMessage());

        }

    }


    public class MessegeImageViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;
        private AppCompatImageView msg;


        public MessegeImageViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            msg = itemView.findViewById(R.id.msg);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void bind(final Message message, final int position) {

            // change orintation
            //   itemView.setLayoutDirection(position % 2 == 0 ? View.LAYOUT_DIRECTION_LTR : View.LAYOUT_DIRECTION_RTL);

            // handle from user meesage
           /* User user;
            if (User.isFromUser(messages.get(position)))
                user = message.getFrom_user();
            else
                user = message.getTo_user();

            // name
            user_name.setText(user.getName());

            // image
            if (user.getImage() != null && !user.getImage().equalsIgnoreCase("null"))
                Glide.with(context)
                        .load(APILinks.Base_Images_Url.getLink() + user.getImage())
                        .into(profile_image);

            // comment msg
            Glide.with(context).load(APILinks.Base_Images_Url.getLink()+message.getMessage()).into(msg);
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        messageImageDelegate.openImages(((BitmapDrawable)msg.getDrawable()).getBitmap());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });*/


        }

    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;

        notifyDataSetChanged();
    }

    public void clearList() {
        messages.clear();
    }

    public void setDelegate(MessageImageDelegate messageImageDelegate) {
        this.messageImageDelegate = messageImageDelegate;
    }

}

