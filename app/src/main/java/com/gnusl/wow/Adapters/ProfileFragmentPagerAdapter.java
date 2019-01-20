package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Fragments.PathProfileFragment;
import com.gnusl.wow.Fragments.PersonalInfoProfileFragment;
import com.gnusl.wow.Fragments.ReceivedGiftsFragment;
import com.gnusl.wow.Fragments.RoomGiftsFragment;
import com.gnusl.wow.Fragments.SentGiftsFragment;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;
    private PagerDelegate pagerDelegate;

    public ProfileFragmentPagerAdapter(Context context, FragmentManager fm, PagerDelegate pagerDelegate) {
        super(fm);
        this.context = context;
        this.pagerDelegate = pagerDelegate;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return PersonalInfoProfileFragment.newInstance();
        else
            return PathProfileFragment.newInstance();
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
            if (pagerDelegate != null)
                pagerDelegate.onReplaceFragment(mCurrentFragment, position);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "Home";
    }

}
