package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.ProfileActivity;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<User> followers;

    public FollowersRecyclerViewAdapter(Context context, ArrayList<User> followers) {
        this.context = context;
        this.followers = followers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.follower_user_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(getFollowers().get(position), position);

    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;

        public UserViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
        }

        public void bind(final User user, final int position) {

            if (user != null) {

                // name
                if (!user.getName().isEmpty())
                    user_name.setText(user.getName());

                // giftUserRank image
                if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                    Glide.with(context)
                            .load(user.getImage_url())
                            .into(profile_image);


//                 go to User
                itemView.setOnClickListener(v -> {

                    goToRoomChannel(user);
                });

            }
        }

        private void goToRoomChannel(User user) {

            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(ProfileActivity.USER_ID, user.getId());
            context.startActivity(intent);
        }
    }

    // region setters and getters

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    // endregion
}
