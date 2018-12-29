package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.MessagesConversationActivity;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.UserMessage;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.gnusl.wow.Views.FontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class UsersMessegesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<UserMessage> userMessages;
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false;

    private static int MESSEGES_HOLDER = 0;
    private static int LOAD_MORE_HOLDER = 1;

    public UsersMessegesAdapter(Context context, List<UserMessage> userMessages, RecyclerView recyclerView, OnLoadMoreListener delegate) {
        this.context = context;
        this.userMessages = userMessages;
        this.loadMoreListener = delegate;

        // setup recycler listener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(-1)) {
                    Log.d("TOP ", true + "");
                } else if (!isLoading && !recyclerView.canScrollVertically(1)) {
                    Log.d("BOTTOM ", true + "");

                    //  onScrollToBottomToLoadMore();
                }

            }
        });

    }

    private void onScrollToBottomToLoadMore() {

        setLoading(true);

        if (loadMoreListener != null)
            loadMoreListener.onLoadMore();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == MESSEGES_HOLDER) {
            view = inflater.inflate(R.layout.messege_view_holder, parent, false);
            return new MessegeViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.load_more_view_holder, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessegeViewHolder)
            ((MessegeViewHolder) holder).bind(userMessages.get(position), position);
        else
            ((LoadingViewHolder) holder).bind();

    }

    @Override
    public int getItemCount() {
        return isLoading ? userMessages.size() + 1 : userMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoading && position == userMessages.size())
            return LOAD_MORE_HOLDER;

        return MESSEGES_HOLDER;
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

    public class MessegeViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parent_layout;

        private FontedTextView name;
        private AutoFitFontedTextView messege;
        private FontedTextView date;
        private CircularImageView profile_image;

        public MessegeViewHolder(View itemView) {

            super(itemView);

            parent_layout = (ConstraintLayout) itemView.findViewById(R.id.parent_layout);
            name = (FontedTextView) itemView.findViewById(R.id.name);
            messege = (AutoFitFontedTextView) itemView.findViewById(R.id.notification_text);
            date = (FontedTextView) itemView.findViewById(R.id.date);
            profile_image = (CircularImageView) itemView.findViewById(R.id.profile_image);
        }

        public void bind(UserMessage userMessage, final int position) {

            if (position % 2 != 0)
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            else
                itemView.setBackgroundColor(context.getResources().getColor(R.color.white));


            // image
            if (userMessage.getUserTo() != null)
                Glide.with(context).load(userMessage.getUserTo().getImage_url()).into(profile_image);

            // name
            if (userMessage.getUserTo() != null)
                name.setText(userMessage.getUserTo().getName());

            // messeges
            messege.setText(userMessage.getMessage());

            // date
            date.setText(userMessage.getCreated_at().split(" ")[0]);

            // set listener
            itemView.setOnClickListener(v -> {

                Intent intent = new Intent(context, MessagesConversationActivity.class);
                intent.putExtra(MessagesConversationActivity.USER_ID, userMessage.getUser_id_to());
                context.startActivity(intent);
            });
        }

    }

    public List<UserMessage> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<UserMessage> userMessages) {
        this.userMessages = userMessages;
        notifyDataSetChanged();
    }

    public void update(List<UserMessage> list) {

        int position = userMessages.size();

        this.userMessages.addAll(list);

        notifyItemInserted(position);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;

        notifyDataSetChanged();
        // notifyItemChanged(posts.size());

    }

}

