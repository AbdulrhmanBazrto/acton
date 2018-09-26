package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Fragments.AllRoomsFragment;
import com.gnusl.wow.Fragments.ExploreRoomsFragment;
import com.gnusl.wow.Fragments.MessagesFragment;
import com.gnusl.wow.Fragments.MomentsFragment;
import com.gnusl.wow.Fragments.NewRoomsFragment;
import com.gnusl.wow.Fragments.RoomFragment;

/**
 * Created by Yehia on 9/27/2018.
 */

public class RoomFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;
    private PagerDelegate pagerDelegate;

    public RoomFragmentPagerAdapter(Context context, FragmentManager fm, PagerDelegate pagerDelegate) {
        super(fm);
        this.context= context;
        this.pagerDelegate=pagerDelegate;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return NewRoomsFragment.newInstance();
        } else if (position == 1){
            return AllRoomsFragment.newInstance();
        } else
            return ExploreRoomsFragment.newInstance();

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
            if(pagerDelegate!=null)
                pagerDelegate.onReplaceFragment(mCurrentFragment,position);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "Home";
    }

}

