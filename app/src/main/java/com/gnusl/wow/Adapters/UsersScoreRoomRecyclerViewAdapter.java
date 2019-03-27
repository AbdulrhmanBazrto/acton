package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Delegates.UserAttendenceDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersScoreRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private UserAttendenceDelegate userAttendenceDelegate;
    private Context context;
    private ArrayList<User> users;

    public UsersScoreRoomRecyclerViewAdapter(Context context, ArrayList<User> users, UserAttendenceDelegate userAttendenceDelegate) {
        this.context = context;
        this.users = users;
        this.userAttendenceDelegate = userAttendenceDelegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.user_score_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(getUsers().get(position), position);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private ImageView user_arist;
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            user_arist = itemView.findViewById(R.id.user_arist);
            userName = itemView.findViewById(R.id.user_name);

        }

        public void bind(final User user, final int position) {

            userName.setText(user.getName());

            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(context)
                        .load(user.getImage_url())
                        .into(userImage);
            else
                userImage.setImageResource(R.drawable.user);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userAttendenceDelegate != null)
                        userAttendenceDelegate.onUserClick(user);
                }
            });

            if (user.getUserAristocracies().size() > 0) {
                user_arist.setVisibility(View.VISIBLE);
                Picasso.with(context).load(user.getUserAristocracies().get(0).getImageUrl()).into(user_arist);
            } else {
                user_arist.setVisibility(View.GONE);
            }

        }

    }

    // region setters and getters

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    // endregion
}

