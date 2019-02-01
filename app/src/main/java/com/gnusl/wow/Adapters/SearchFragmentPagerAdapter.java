package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Fragments.SearchRoomFragment;
import com.gnusl.wow.Fragments.UsersFragment;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private Fragment mCurrentFragment;
    private PagerDelegate pagerDelegate;
    private SearchRoomFragment searchRoomFragment;
    private UsersFragment usersFragment;

    public SearchFragmentPagerAdapter(Context context, FragmentManager fm,PagerDelegate pagerDelegate) {
        super(fm);
        this.context= context;
        this.pagerDelegate=pagerDelegate;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return searchRoomFragment;
        } else
            return usersFragment;

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
            if(pagerDelegate!=null)
                pagerDelegate.onReplaceFragment(mCurrentFragment,position);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return "Home";
    }

    public SearchRoomFragment getSearchRoomFragment() {
        return searchRoomFragment;
    }

    public void setSearchRoomFragment(SearchRoomFragment searchRoomFragment) {
        this.searchRoomFragment = searchRoomFragment;
    }

    public UsersFragment getUsersFragment() {
        return usersFragment;
    }

    public void setUsersFragment(UsersFragment usersFragment) {
        this.usersFragment = usersFragment;
    }
}


