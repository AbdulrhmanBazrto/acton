package com.gnusl.wow.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class GiftUsersRankingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<GiftUserRank> giftUsers;

    public GiftUsersRankingRecyclerViewAdapter(Context context, ArrayList<GiftUserRank> giftUsers) {
        this.context = context;
        this.giftUsers = giftUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.gift_user_rank_view_holder, parent, false);
        return new UserGiftViewHolder (view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserGiftViewHolder ) holder).bind(getGiftUsers().get(position), position);

    }

    @Override
    public int getItemCount() { 
        return giftUsers.size();
    }

    public class UserGiftViewHolder extends RecyclerView.ViewHolder {

        private TextView rank;
        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;
        private AutoFitFontedTextView amount;

        public UserGiftViewHolder (View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            amount = itemView.findViewById(R.id.amount);
        }

        public void bind(final GiftUserRank giftUserRank, final int position) {

            if (giftUserRank != null) {

                // rank
                rank.setText(String.valueOf(position+1));

                // name
                if (giftUserRank.getUser() != null)
                    user_name.setText(giftUserRank.getUser().getName());

                // total
                amount.setText(giftUserRank.getTotal());

                // giftUserRank image
                if (giftUserRank.getUser() != null && !giftUserRank.getUser().getImage_url().isEmpty())
                    Glide.with(context)
                            .load(giftUserRank.getUser().getImage_url())
                            .into(profile_image);


                // go to Chat GiftUserRank
               /* itemView.setOnClickListener(v -> {

                    goToRoomChannel(giftUserRank);
                });*/

            }
        }

        private void goToRoomChannel(GiftUserRank giftUserRank){

           /* Intent intent=new Intent(context, RoomChatActivity.class);
            intent.putExtra(RoomChatActivity.CHANNEL_KEY,giftUserRank);
            context.startActivity(intent);*/
        }
    }

    // region setters and getters

    public ArrayList<GiftUserRank> getGiftUsers() {
        return giftUsers;
    }

    public void setGiftUsers(ArrayList<GiftUserRank> giftUsers) {
        this.giftUsers = giftUsers;
    }


    // endregion
}

