package com.gnusl.wow.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.GiftRoomsRankingRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class RoomTopDayGiftsFragment extends Fragment {


    private View inflatedView;
    private GiftRoomsRankingRecyclerViewAdapter giftRoomsRankingRecyclerViewAdapter;
    private int roomId;

    public RoomTopDayGiftsFragment() {

    }

    public static RoomTopDayGiftsFragment newInstance(int roomId) {

        RoomTopDayGiftsFragment roomTopDayGiftsFragment = new RoomTopDayGiftsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("roomId", roomId);
        roomTopDayGiftsFragment.setArguments(bundle);
        return roomTopDayGiftsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_day_gifts, container, false);

        // initialize adapter
        RecyclerView recyclerView = inflatedView.findViewById(R.id.ranking_users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        giftRoomsRankingRecyclerViewAdapter = new GiftRoomsRankingRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(giftRoomsRankingRecyclerViewAdapter);

        if (getArguments() != null)
            roomId = getArguments().getInt("roomId");

        getGifts();

        return inflatedView;
    }

    private void getGifts() {
        APIConnectionNetwork.GetRoomTopGiftUser(roomId, new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
            }

            @Override
            public void onConnectionError(ANError anError) {
            }

            @Override
            public void onConnectionSuccess(String response) {
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                ArrayList<GiftRoomRank> user_sent_week = GiftRoomRank.parseJSONArray(jsonObject.optJSONArray("user_sent_today"));
                giftRoomsRankingRecyclerViewAdapter.setGiftRoomRanks(user_sent_week);
                giftRoomsRankingRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            getGifts();
    }


}

