package com.gnusl.wow.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.GiftsActivity;
import com.gnusl.wow.Adapters.GiftRoomsRankingRecyclerViewAdapter;
import com.gnusl.wow.Adapters.GiftUsersRankingRecyclerViewAdapter;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class WeekGiftsFragment extends Fragment {

    private GiftsActivity giftsActivity;
    private View inflatedView;
    GiftUsersRankingRecyclerViewAdapter giftUsersRankingRecyclerViewAdapter;
    private GiftRoomsRankingRecyclerViewAdapter giftRoomsRankingRecyclerViewAdapter;
    private Fragment parentFragment;
    private ConstraintLayout content_container;

    @SuppressLint("ValidFragment")
    public WeekGiftsFragment(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    public static WeekGiftsFragment newInstance(Fragment parentFragment) {

        return new WeekGiftsFragment(parentFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        giftsActivity = (GiftsActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_week_gifts, container, false);

        // initialize adapter
        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.ranking_users_recycler_view);
        content_container = inflatedView.findViewById(R.id.content_container);
        content_container.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(parentFragment instanceof RoomGiftsFragment){

            giftRoomsRankingRecyclerViewAdapter = new GiftRoomsRankingRecyclerViewAdapter(getContext(), new ArrayList<>());
            recyclerView.setAdapter(giftRoomsRankingRecyclerViewAdapter);

        }else {
            giftUsersRankingRecyclerViewAdapter = new GiftUsersRankingRecyclerViewAdapter(getContext(), new ArrayList<>());
            recyclerView.setAdapter(giftUsersRankingRecyclerViewAdapter);
        }

        return inflatedView;
    }

    public void refreshUsersData(ArrayList<GiftUserRank> giftUserRanks) {

        if(giftUserRanks==null) {
            content_container.setVisibility(View.GONE);
            return;
        }

        if(giftUserRanks.isEmpty()) {
            content_container.setVisibility(View.GONE);
            return;
        }

        content_container.setVisibility(View.VISIBLE);
        giftUsersRankingRecyclerViewAdapter.setGiftUsers(giftUserRanks);
        giftUsersRankingRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void refreshRoomsData(ArrayList<GiftRoomRank> giftRoomRanks){

        if(giftRoomRanks==null) {
            content_container.setVisibility(View.GONE);
            return;
        }

        if(giftRoomRanks.isEmpty()) {
            content_container.setVisibility(View.GONE);
            return;
        }

        content_container.setVisibility(View.VISIBLE);
        giftRoomsRankingRecyclerViewAdapter.setGiftRoomRanks(giftRoomRanks);
        giftRoomsRankingRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void refreshData() {

        if (parentFragment instanceof ReceivedGiftsFragment)
            refreshUsersData(giftsActivity.getWeekReceivedUserRanks());
        else if (parentFragment instanceof SentGiftsFragment)
            refreshUsersData(giftsActivity.getWeekSentUserRanks());
        else
            refreshRoomsData(giftsActivity.getWeekRoomRanks());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

            refreshData();
        }
    }

    @Subscribe
    public void onRefreshEvent(RefreshGiftsDelegate refreshGiftsDelegate) {

        if (getUserVisibleHint())
            refreshData();
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }
}

