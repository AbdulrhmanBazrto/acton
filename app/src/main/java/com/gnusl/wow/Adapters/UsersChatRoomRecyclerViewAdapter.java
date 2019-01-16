package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class UsersChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<User> users;

    public UsersChatRoomRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.user_chat_room_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder)holder).bind(getUsers().get(position), position);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView userImage;
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);

            userImage= itemView.findViewById(R.id.user_image);
            userName= itemView.findViewById(R.id.user_name);

        }

        public void bind(final User user, final int position) {

            // name
            userName.setText(user.getName());

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(context)
                        .load(user.getImage_url())
                        .into(userImage);
        }

    }

    // region setters and getters

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    // endregion
}
