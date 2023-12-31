package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.MyRoomsActivity;
import com.gnusl.wow.Adapters.AdvertisementPagerAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Advertisement;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.GraphicsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Yehia on 9/27/2018.
 */

public class AllRoomsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private RoomsRecyclerViewAdapter roomsRecyclerViewAdapter;
    private ViewPager advertisementViewPager;
    private AdvertisementPagerAdapter advertisementPagerAdapter;

    public AllRoomsFragment() {
    }

    public static AllRoomsFragment newInstance() {

        return new AllRoomsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_all_rooms, container, false);

        // initialize adapter
        RecyclerView recyclerView = inflatedView.findViewById(R.id.all_rooms_recycler_view);
        advertisementViewPager = inflatedView.findViewById(R.id.ads_pager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(roomsRecyclerViewAdapter);

        advertisementPagerAdapter = new AdvertisementPagerAdapter(getContext(), new ArrayList<>());
        advertisementViewPager.setAdapter(advertisementPagerAdapter);


        inflatedView.findViewById(R.id.cl_my_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyRoomsActivity.class));
            }
        });

        return inflatedView;
    }


    private void sendChannelsRequest() {

        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetAllChannels(null, this);
    }

    private void sendAdvertisementRequest() {

        // send request
        APIConnectionNetwork.GetAdvertisement(new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) advertisementViewPager.getLayoutParams();
                layoutParams.height = GraphicsUtil.pxFromDp(0f);
                advertisementViewPager.setLayoutParams(layoutParams);
            }

            @Override
            public void onConnectionError(ANError anError) {

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) advertisementViewPager.getLayoutParams();
                layoutParams.height = GraphicsUtil.pxFromDp(0f);
                advertisementViewPager.setLayoutParams(layoutParams);
            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

                ArrayList<Advertisement> advertisements = Advertisement.parseJSONArray(jsonArray);

                if (!advertisements.isEmpty()) {

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) advertisementViewPager.getLayoutParams();
                    layoutParams.height = GraphicsUtil.pxFromDp(100f);
                    advertisementViewPager.setLayoutParams(layoutParams);

                    advertisementPagerAdapter.setAdvertisements(advertisements);
                    advertisementPagerAdapter.notifyDataSetChanged();
                } else {

                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) advertisementViewPager.getLayoutParams();
                    layoutParams.height = GraphicsUtil.pxFromDp(0f);
                    advertisementViewPager.setLayoutParams(layoutParams);

                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null && roomsRecyclerViewAdapter != null && roomsRecyclerViewAdapter.getRooms().isEmpty()) {

            // send request
            sendChannelsRequest();

            // get advertisement
            sendAdvertisementRequest();
        }
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<Room> rooms = Room.parseJSONArray(jsonArray);

        // notify
        roomsRecyclerViewAdapter.setRooms(rooms);
        roomsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        LoaderPopUp.dismissLoader();
    }
}

