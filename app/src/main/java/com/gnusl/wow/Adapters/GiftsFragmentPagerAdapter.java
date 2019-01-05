package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Fragments.DayGiftsFragment;
import com.gnusl.wow.Fragments.MonthGiftsFragment;
import com.gnusl.wow.Fragments.WeekGiftsFragment;

import org.json.JSONArray;

public class GiftsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;
    private PagerDelegate pagerDelegate;
    private Fragment parentFragment;

    public GiftsFragmentPagerAdapter(Context context, FragmentManager fm, Fragment parentFragment,PagerDelegate pagerDelegate) {
        super(fm);
        this.context= context;
        this.pagerDelegate=pagerDelegate;
        this.parentFragment=parentFragment;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return MonthGiftsFragment.newInstance(parentFragment);
        else if (position == 1)
            return WeekGiftsFragment.newInstance(parentFragment);
        else
            return DayGiftsFragment.newInstance(parentFragment);
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


