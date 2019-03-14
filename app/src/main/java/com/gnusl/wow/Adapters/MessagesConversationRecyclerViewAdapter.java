package com.gnusl.wow.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.gnusl.wow.Utils.SharedPreferencesUtils;
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

    private static int MESSAGE_HOLDER_LTR = 0;
    private static int MESSAGE_HOLDER_RTL = 1;
    private static int MESSAGE_IMAGE_HOLDER_LTR = 3;
    private static int MESSAGE_IMAGR_HOLDER_RTL = 4;


    MessageImageDelegate messageImageDelegate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MessagesConversationRecyclerViewAdapter(Context context, List<Message> messages, OnLoadMoreListener delegate) {
        this.context = context;
        this.messages = messages;
        this.loadMoreListener = delegate;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
            ((MessegeViewHolder) holder).bind(messages.get(position), position);
        else if (holder instanceof MessegeImageViewHolder)
            ((MessegeImageViewHolder) holder).bind(messages.get(position), position);
        else
            ((LoadingViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (messages.get(position).getUser_id_from() == SharedPreferencesUtils.getUser().getId())
            return MESSAGE_HOLDER_LTR;
        else
            return MESSAGE_HOLDER_RTL;
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

            if (position == 3) {
                if (loadMoreListener != null)
                    loadMoreListener.onLoadMore();
            }

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

    public void addMessages(List<Message> messages) {
        this.messages.addAll(0, messages);
    }


    public void setDelegate(MessageImageDelegate messageImageDelegate) {
        this.messageImageDelegate = messageImageDelegate;
    }

}

