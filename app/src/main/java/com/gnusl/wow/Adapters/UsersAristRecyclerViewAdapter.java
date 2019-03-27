package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gnusl.wow.Models.Aristocracy;
import com.gnusl.wow.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersAristRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Aristocracy> users;

    public UsersAristRecyclerViewAdapter(Context context, List<Aristocracy> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.user_arist_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(users.get(position), position);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_arist;

        public UserViewHolder(View itemView) {
            super(itemView);
            user_arist = itemView.findViewById(R.id.user_arist);

        }

        public void bind(final Aristocracy user, final int position) {


            Picasso.with(context).load(user.getImageUrl()).into(user_arist);


        }

    }


}

