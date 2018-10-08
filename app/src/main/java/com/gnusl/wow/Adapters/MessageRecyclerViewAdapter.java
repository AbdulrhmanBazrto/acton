package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Message> messages;

    public MessageRecyclerViewAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.message_view_holder, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((MessageViewHolder)holder).bind(getMessages().get(position), position);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView imageIcon;
        private TextView content;

        public MessageViewHolder(View itemView) {
            super(itemView);

            imageIcon=(AppCompatImageView) itemView.findViewById(R.id.image_icon);
            content=(TextView) itemView.findViewById(R.id.content_text);

        }

        public void bind(final Message message, final int position) {

            imageIcon.setImageResource(message.getImageResource());

            content.setText(message.getContent());
        }

    }

    // region setters and getters

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    // endregion
}

