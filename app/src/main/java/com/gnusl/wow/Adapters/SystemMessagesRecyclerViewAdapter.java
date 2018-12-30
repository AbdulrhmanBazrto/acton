package com.gnusl.wow.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.gnusl.wow.Delegates.MessageImageDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.SystemMessage;
import com.gnusl.wow.Models.SystemMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.gnusl.wow.Views.FontedTextView;
import com.klinker.android.sliding.TouchlessScrollView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class SystemMessagesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SystemMessage> messages;

    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false;
    private int updatedPosition;

    private static int MESSAGE_HOLDER = 0;
    private static int LOAD_MORE_HOLDER = 2;

    MessageImageDelegate messageImageDelegate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public SystemMessagesRecyclerViewAdapter(Context context, List<SystemMessage> messages, ViewGroup scrollView, OnLoadMoreListener delegate) {
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
                ((TouchlessScrollView) scrollView).setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                        // We take the last son in the scrollview
                        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
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
        if (viewType == MESSAGE_HOLDER) {
            view = inflater.inflate(R.layout.system_message_view_holder, parent, false);
            return new MessegeViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.load_more_view_holder, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessegeViewHolder)
            ((MessegeViewHolder) holder).bind(messages.get(updatedPosition), updatedPosition);
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

        return MESSAGE_HOLDER;
    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.load_more_progress);
            progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        public void bind() {
        }
    }

    public class MessegeViewHolder extends RecyclerView.ViewHolder {

        private AutoFitFontedTextView dateTv;
        private FontedTextView msg;

        public MessegeViewHolder(View itemView) {
            super(itemView);

            dateTv = (AutoFitFontedTextView) itemView.findViewById(R.id.date_text_view);
            msg = (FontedTextView) itemView.findViewById(R.id.msg);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void bind(final SystemMessage message, final int position) {

            // handle from user meesage

            dateTv.setText(message.getCreated_at());

            // msg
            msg.setText(message.getSystemMessage());

        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;

        notifyDataSetChanged();
    }

    public List<SystemMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<SystemMessage> messages) {
        this.messages = messages;
    }

    public void clearList() {
        messages.clear();
    }

    public void setDelegate(MessageImageDelegate messageImageDelegate) {
        this.messageImageDelegate = messageImageDelegate;
    }

}