package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.ProfileActivity;
import com.gnusl.wow.Connection.APILinks;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int TEXT_MESSAGE_HOLDER = 0;
    private final static int IMAGE_MESSAGE_HOLDER = 1;

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

        if (viewType == TEXT_MESSAGE_HOLDER) {
            view = inflater.inflate(R.layout.chat_message_view_holder, parent, false);
            return new MessageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.chat_image_message_view_holder, parent, false);
            return new ImageMessageViewHolder(view);
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

        if (getChatMessages().get(position).isImageMessage())
            return IMAGE_MESSAGE_HOLDER;
        else
            return TEXT_MESSAGE_HOLDER;
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
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
            userImage.setOnClickListener(v->{

                if(chatMessage.getUser()!=null){
                    Intent intent=new Intent(context,ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID,chatMessage.getUser().getId());
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
            userImage.setOnClickListener(v->{

                if(chatMessage.getUser()!=null){
                    Intent intent=new Intent(context,ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID,chatMessage.getUser().getId());
                    context.startActivity(intent);
                }
            });
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
