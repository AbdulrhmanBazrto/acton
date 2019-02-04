package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Delegates.MicUserDelegate;
import com.gnusl.wow.Models.MicUser;
import com.gnusl.wow.Models.MicUser;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class MicUsersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MicUser> micUsers;
    private MicUserDelegate micUserDelegate;

    private static int MIC_HOLDER = 0;
    private static int USER_HOLDER = 1;

    public MicUsersRecyclerViewAdapter(Context context, ArrayList<MicUser> micUsers, MicUserDelegate micUserDelegate) {
        this.context = context;
        this.micUsers = micUsers;
        this.micUserDelegate = micUserDelegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == USER_HOLDER) {
            view = inflater.inflate(R.layout.user_chat_room_view_holder, parent, false);
            return new UserViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.mic_without_user_view_holder, parent, false);
            return new MicViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof UserViewHolder)
            ((UserViewHolder) holder).bind(getMicUsers().get(position), position);
        else if (holder instanceof MicViewHolder)
            ((MicViewHolder) holder).bind(getMicUsers().get(position), position);

    }

    @Override
    public int getItemViewType(int position) {

        if (getMicUsers().get(position).getUser() != null)
            return USER_HOLDER;
        else
            return MIC_HOLDER;
    }

    @Override
    public int getItemCount() {
        return micUsers.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView userImage;
        private TextView userName;

        public UserViewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);

        }

        public void bind(final MicUser micUser, final int position) {

            // name
            userName.setText(micUser.getUser().getName());

            // user image
            if (micUser.getUser().getImage_url() != null && !micUser.getUser().getImage_url().isEmpty())
                Glide.with(context)
                        .load(micUser.getUser().getImage_url())
                        .into(userImage);

            itemView.setOnClickListener(v -> {

                if (micUserDelegate != null)
                    micUserDelegate.onSelectUserOnMic(micUser);
            });
        }

    }

    public class MicViewHolder extends RecyclerView.ViewHolder {

        public MicViewHolder(View itemView) {
            super(itemView);

        }

        public void bind(final MicUser micUser, final int position) {

            itemView.setOnClickListener(v -> {

                if (micUserDelegate != null)
                    micUserDelegate.onTakeMic(micUser.getMicId());
            });
        }

    }

    // region setters and getters

    public ArrayList<MicUser> getMicUsers() {
        return micUsers;
    }

    public void setMicUsers(ArrayList<MicUser> micUsers) {
        this.micUsers = micUsers;
    }


    // endregion
}
