package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.wow.Fragments.RoomTopDayGiftsFragment;
import com.gnusl.wow.Fragments.RoomTopWeekGiftsFragment;

public class RoomGiftsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;
    private int roomId;

    public RoomGiftsFragmentPagerAdapter(Context context, FragmentManager fm, int roomId) {
        super(fm);
        this.context = context;
        this.roomId = roomId;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return RoomTopWeekGiftsFragment.newInstance(roomId);
        else
            return RoomTopDayGiftsFragment.newInstance(roomId);
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "Home";
    }
}


