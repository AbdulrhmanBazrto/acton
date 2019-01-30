package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.ProfileActivity;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.logging.Handler;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TEXT_MESSAGE_HOLDER = 0;
    private final static int IMAGE_MESSAGE_HOLDER = 1;
    private static int LOAD_MORE_HOLDER = 2;

    private Context context;
    private boolean isLoading = false;
    private OnLoadMoreListener loadMoreListener;
    private ArrayList<ChatMessage> chatMessages;

    public ChatRecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<ChatMessage> chatMessages,OnLoadMoreListener delegate) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.loadMoreListener = delegate;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == TEXT_MESSAGE_HOLDER) {
            view = inflater.inflate(R.layout.chat_message_view_holder, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == IMAGE_MESSAGE_HOLDER) {
            view = inflater.inflate(R.layout.chat_image_message_view_holder, parent, false);
            return new ImageMessageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.load_more_view_holder, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessageViewHolder)
            ((MessageViewHolder) holder).bind(getChatMessages().get(position), position);
        else if (holder instanceof ImageMessageViewHolder)
            ((ImageMessageViewHolder) holder).bind(getChatMessages().get(position), position);
    }

    @Override
    public int getItemViewType(int position) {

        if (isLoading && position == chatMessages.size())
            return LOAD_MORE_HOLDER;
        else if (getChatMessages().get(position).isImageMessage())
            return IMAGE_MESSAGE_HOLDER;
        else
            return TEXT_MESSAGE_HOLDER;
    }

    @Override
    public int getItemCount() {
        return isLoading ? chatMessages.size() + 1 : chatMessages.size();
    }

    private void onScrollToBottomToLoadMore() {

        setLoading(true);

        if (loadMoreListener != null)
            loadMoreListener.onLoadMore();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;
        private TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            messageTextView = itemView.findViewById(R.id.message_text_view);

        }

        public void bind(final ChatMessage chatMessage, final int position) {

            // user name
            String name = chatMessage.getUserName() != null ? chatMessage.getUserName() : (chatMessage.getUser() != null ? chatMessage.getUser().getName() : "");
            userName.setText(name);

            // message
            messageTextView.setText(chatMessage.getMessage());

            // user image
            if (chatMessage.getUserImage() != null && !chatMessage.getUserImage().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getUserImage())
                        .into(userImage);
            else if (chatMessage.getUser() != null && chatMessage.getUser().getImage_url() != null && !chatMessage.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getUser().getImage_url())
                        .into(userImage);

            // go to profile
            userImage.setOnClickListener(v -> {

                if (chatMessage.getUser() != null) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, chatMessage.getUser().getId());
                    context.startActivity(intent);
                }
            });

        }

    }

    public class ImageMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;
        private ImageView imageMessage;

        public ImageMessageViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            imageMessage = itemView.findViewById(R.id.image_msg);

        }

        public void bind(final ChatMessage chatMessage, final int position) {

            // user name
            String name = chatMessage.getUserName() != null ? chatMessage.getUserName() : (chatMessage.getUser() != null ? chatMessage.getUser().getName() : "");
            userName.setText(name);

            // image message
            if (chatMessage.getMessage() != null && !chatMessage.getMessage().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getMessage())
                        .into(imageMessage);

            // gift image message
            if (chatMessage.getGiftImagePath() != null && !chatMessage.getGiftImagePath().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getGiftImagePath())
                        .into(imageMessage);

            // user image
            if (chatMessage.getUserImage() != null && !chatMessage.getUserImage().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getUserImage())
                        .into(userImage);
            else if (chatMessage.getUser() != null && chatMessage.getUser().getImage_url() != null && !chatMessage.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getUser().getImage_url())
                        .into(userImage);

            // go to profile
            userImage.setOnClickListener(v -> {

                if (chatMessage.getUser() != null) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, chatMessage.getUser().getId());
                    context.startActivity(intent);
                }
            });
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

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    // endregion
}
