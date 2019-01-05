package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class GiftRoomsRankingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<GiftRoomRank> giftRoomRanks;

    public GiftRoomsRankingRecyclerViewAdapter(Context context, ArrayList<GiftRoomRank> giftRoomRanks) {
        this.context = context;
        this.giftRoomRanks = giftRoomRanks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.gift_user_rank_view_holder, parent, false);
        return new UserGiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((UserGiftViewHolder) holder).bind(getGiftRoomRanks().get(position), position);

    }

    @Override
    public int getItemCount() {
        return giftRoomRanks.size();
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

        public void bind(final GiftRoomRank giftUserRank, final int position) {

            if (giftUserRank != null) {

                // rank
                rank.setText(String.valueOf(position+1));

                // name
                if (giftUserRank.getRoom() != null)
                    user_name.setText(giftUserRank.getRoom().getName());

                // total
                amount.setText(giftUserRank.getTotal());

                // giftUserRank image
                if (giftUserRank.getRoom() != null && !giftUserRank.getRoom().getBackgroundUrl().isEmpty())
                    Glide.with(context)
                            .load(giftUserRank.getRoom().getBackgroundUrl())
                            .into(profile_image);


                // go to Chat GiftRoomRank
               /* itemView.setOnClickListener(v -> {

                    goToRoomChannel(giftUserRank);
                });*/

            }
        }

        private void goToRoomChannel(GiftRoomRank giftUserRank){

           /* Intent intent=new Intent(context, RoomChatActivity.class);
            intent.putExtra(RoomChatActivity.CHANNEL_KEY,giftUserRank);
            context.startActivity(intent);*/
        }
    }

    // region setters and getters

    public ArrayList<GiftRoomRank> getGiftRoomRanks() {
        return giftRoomRanks;
    }

    public void setGiftRoomRanks(ArrayList<GiftRoomRank> giftRoomRanks) {
        this.giftRoomRanks = giftRoomRanks;
    }

    // endregion
}


