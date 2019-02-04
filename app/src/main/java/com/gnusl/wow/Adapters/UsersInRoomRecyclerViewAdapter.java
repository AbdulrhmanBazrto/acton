package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gnusl.wow.Activities.ProfileActivity;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class UsersInRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<User> users;

    public UsersInRoomRecyclerViewAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.user_view_holder, parent, false);
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
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);

        }

        public void bind(final User user, final int position) {


            // name
            if (user.getName() != null && !user.getName().isEmpty())
                userName.setText(user.getName());

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(context)
                        .load(user.getImage_url())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                userImage.setImageResource(R.drawable.ic_launcher_wow);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                if (resource != null)
                                    userImage.setImageDrawable(resource);
                                else
                                    userImage.setImageResource(R.drawable.ic_launcher_wow);
                                return false;
                            }
                        })
                        .into(userImage);

            // go to profile
            itemView.setOnClickListener(v -> {

                if (user != null) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, user.getId());
                    context.startActivity(intent);
                }
            });

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
