package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.SearchActivity;
import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room, container, false);

        // user info
        setUserInformation();

        // initialize top bar with viewpager
        initializeSmartTabs();

        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v->((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START));

        // open search
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.search_icon).setOnClickListener(v->{

                Intent intent=new Intent(getActivity(),SearchActivity.class);
                intent.putExtra(SearchActivity.SEARCH_FOR_ROOMS,"");
                startActivity(intent);
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

}

