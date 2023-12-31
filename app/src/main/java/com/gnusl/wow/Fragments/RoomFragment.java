package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.SearchActivity;
import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Delegates.HideDialyButton;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.DialyLoginPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yehia on 9/23/2018.
 */

public class RoomFragment extends Fragment implements SmartTabLayout.TabProvider {

    private View inflatedView;

    public RoomFragment() {
    }

    public static RoomFragment newInstance() {

        return new RoomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room, container, false);

        // user info
        setUserInformation();

        // initialize top bar with viewpager
        initializeSmartTabs();

        // handling daily login dialog
        DialyLoginPopUp.show(getActivity());

        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v -> ((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START));

        // open search
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.search_icon).setOnClickListener(v -> {

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(SearchActivity.SEARCH_FOR_ROOMS, "");
                startActivity(intent);
            });


        try {
            JSONArray loginJsonArray = null;
            String dailyLoginArrayStr = SharedPreferencesUtils.getDailyLoginArray();
            if (dailyLoginArrayStr.equalsIgnoreCase(""))
                loginJsonArray = new JSONArray();
            else
                loginJsonArray = new JSONArray(dailyLoginArrayStr);

            if (loginJsonArray.length() != 0) {
                JSONObject lastLogin = loginJsonArray.getJSONObject(loginJsonArray.length() - 1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                if (lastLogin != null && lastLogin.optLong("timestamp") > calendar.getTimeInMillis()) {
                    inflatedView.findViewById(R.id.btn_showDailyLogin).setVisibility(View.GONE);
                } else {
                    inflatedView.findViewById(R.id.btn_showDailyLogin).setVisibility(View.VISIBLE);
                }
            } else {
                inflatedView.findViewById(R.id.btn_showDailyLogin).setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {

        }
        inflatedView.findViewById(R.id.btn_showDailyLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialyLoginPopUp.show(getActivity());
            }
        });

        return inflatedView;
    }

    private void initializeSmartTabs() {

        RoomFragmentPagerAdapter roomFragmentPagerAdapter = new RoomFragmentPagerAdapter(getContext(), getFragmentManager(), null);

        ViewPager viewPager = inflatedView.findViewById(R.id.roomviewpager);
        viewPager.setAdapter(roomFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = inflatedView.findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }

    private void setUserInformation() {

        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(this)
                        .load(user.getImage_url())
                        .into(((ImageView) inflatedView.findViewById(R.id.right_icon)));
        }
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:
                fontedTextView.setText(getString(R.string.new_text));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.all));
                break;

            case 2:

                fontedTextView.setText(getString(R.string.explore));
                break;

        }

        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

    @Subscribe
    public void onLogin(HideDialyButton hideDialyButton) {
        inflatedView.findViewById(R.id.btn_showDailyLogin).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}

