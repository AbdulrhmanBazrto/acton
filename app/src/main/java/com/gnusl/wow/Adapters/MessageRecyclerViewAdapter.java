package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Delegates.MessageSectionDelegate;
import com.gnusl.wow.Models.MessageSection;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageSection> messageSections;
    private MessageSectionDelegate messageSectionDelegate;

    public MessageRecyclerViewAdapter(Context context, ArrayList<MessageSection> messageSections,MessageSectionDelegate messageSectionDelegate) {
        this.context = context;
        this.messageSections = messageSections;
        this.messageSectionDelegate=messageSectionDelegate;
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

        ((MessageViewHolder) holder).bind(getMessageSections().get(position), position);

    }

    @Override
    public int getItemCount() {
        return messageSections.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView imageIcon;
        private TextView content;

        public MessageViewHolder(View itemView) {
            super(itemView);

            imageIcon = (AppCompatImageView) itemView.findViewById(R.id.image_icon);
            content = (TextView) itemView.findViewById(R.id.content_text);

        }

        public void bind(final MessageSection messageSection, final int position) {

            imageIcon.setImageResource(messageSection.getImageResource());

            content.setText(messageSection.getContent());

            itemView.setOnClickListener(v->{

                switch (messageSection.getImageResource()) {

                    case R.drawable.friends:

                        messageSectionDelegate.onClickFriendsSection();
                        break;

                    case R.drawable.system:

                        break;
                }
            });
        }

    }

    // region setters and getters

    public ArrayList<MessageSection> getMessageSections() {
        return messageSections;
    }

    public void setMessageSections(ArrayList<MessageSection> messageSections) {
        this.messageSections = messageSections;
    }

    // endregion
}

