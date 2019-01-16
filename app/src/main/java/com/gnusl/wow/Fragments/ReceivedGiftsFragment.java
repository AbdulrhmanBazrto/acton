package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Activities.GiftsActivity;
import com.gnusl.wow.Adapters.GiftsFragmentPagerAdapter;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivedGiftsFragment extends Fragment implements SmartTabLayout.TabProvider {

    private View inflatedView;
    private GiftsFragmentPagerAdapter giftsFragmentPagerAdapter;

    public ReceivedGiftsFragment() {
    }

    public static ReceivedGiftsFragment newInstance() {

        return new ReceivedGiftsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_recieved_gifts, container, false);

        // initialize top bar with viewpager
        initializeSmartTabs();

        return inflatedView;
    }

    private void initializeSmartTabs() {

        giftsFragmentPagerAdapter = new GiftsFragmentPagerAdapter(getContext(), getFragmentManager(), this,null);

        ViewPager viewPager = inflatedView.findViewById(R.id.recieved_gifts_scrolling_view_pager);
        viewPager.setAdapter(giftsFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = inflatedView.findViewById(R.id.view_pager_tab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:
                fontedTextView.setText(getString(R.string.month));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.week));
                break;

            case 2:

                fontedTextView.setText(getString(R.string.day));
                break;

        }

        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

            // send event
            EventBus.getDefault().post(new RefreshGiftsDelegate());
        }
    }

    public GiftsFragmentPagerAdapter getGiftsFragmentPagerAdapter() {
        return giftsFragmentPagerAdapter;
    }

    public void setGiftsFragmentPagerAdapter(GiftsFragmentPagerAdapter giftsFragmentPagerAdapter) {
        this.giftsFragmentPagerAdapter = giftsFragmentPagerAdapter;
    }

}
