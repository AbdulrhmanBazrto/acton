package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ChatMessage> chatMessages;

    public ChatRecyclerViewAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.chat_message_view_holder, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MessageViewHolder) holder).bind(getChatMessages().get(position), position);

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView userImage;
        private TextView userName;
        private TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);

            userImage = (AppCompatImageView) itemView.findViewById(R.id.user_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            messageTextView = (TextView) itemView.findViewById(R.id.message_text_view);

        }

        public void bind(final ChatMessage chatMessage, final int position) {

            // user name
            if (chatMessage.getUser() != null)
                userName.setText(chatMessage.getUser().getName());

            // message
            messageTextView.setText(chatMessage.getMessage());

            // user image
            if (chatMessage.getUser() != null && chatMessage.getUser().getImage_url() != null && !chatMessage.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(chatMessage.getUser().getImage_url())
                        .into(userImage);

        }

    }

    // region setters and getters

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }


    // endregion
}
