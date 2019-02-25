package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Adapters.GiftsFragmentPagerAdapter;
import com.gnusl.wow.Adapters.RoomGiftsFragmentPagerAdapter;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class RoomTopGiftsFragment extends Fragment implements SmartTabLayout.TabProvider {

    private View inflatedView;
    private RoomGiftsFragmentPagerAdapter roomGiftsFragmentPagerAdapter;
    private int roomId;

    public RoomTopGiftsFragment() {
        if (getArguments() != null)
            roomId = getArguments().getInt("roomId");
    }

    public static RoomTopGiftsFragment newInstance(int roomId) {
        RoomTopGiftsFragment roomTopGiftsFragment = new RoomTopGiftsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("roomId", roomId);
        roomTopGiftsFragment.setArguments(bundle);

        return roomTopGiftsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_sent_gifts, container, false);

        // initialize top bar with viewpager
        initializeSmartTabs();

        return inflatedView;
    }

    private void initializeSmartTabs() {

        roomGiftsFragmentPagerAdapter = new RoomGiftsFragmentPagerAdapter(getContext(), getFragmentManager(), roomId);

        ViewPager viewPager = inflatedView.findViewById(R.id.sent_gifts_scrolling_view_pager);
        viewPager.setAdapter(roomGiftsFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = inflatedView.findViewById(R.id.view_pager_tab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {
            case 0:

                fontedTextView.setText(getString(R.string.week));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.day));
                break;

        }

        return inflatedView;
    }

}

