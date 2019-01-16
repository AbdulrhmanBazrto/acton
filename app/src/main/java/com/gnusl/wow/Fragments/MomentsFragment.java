package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.CreatePostActivity;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Adapters.MomentsFragmentPagerAdapter;
import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by Yehia on 9/23/2018.
 */

public class MomentsFragment extends Fragment implements SmartTabLayout.TabProvider{

    private View inflatedView;

    public MomentsFragment() {
    }

    public static MomentsFragment newInstance() {

        return new MomentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_moments, container, false);

        AppCompatImageView search_icon= inflatedView.findViewById(R.id.search_icon);
        search_icon.setImageResource(R.drawable.ic_action_camera);

        // user info
        setUserInformation();

        // initialize top bar with viewpager
        initializeSmartTabs();

        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v->((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START));

        // open create post
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.search_icon).setOnClickListener(v->goToCreatePostActivity());

        return inflatedView;
    }

    private void initializeSmartTabs(){

        MomentsFragmentPagerAdapter momentsFragmentPagerAdapter=new MomentsFragmentPagerAdapter(getContext(),getFragmentManager(),null);

        ViewPager viewPager = inflatedView.findViewById(R.id.moments_view_pager);
        viewPager.setAdapter(momentsFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = inflatedView.findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

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
                fontedTextView.setText(getString(R.string.following));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.featured));
                break;

        }

        return inflatedView;
    }

    private void goToCreatePostActivity(){

        getActivity().startActivity(new Intent(getActivity(),CreatePostActivity.class));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }


}
