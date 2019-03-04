package com.gnusl.wow.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Adapters.MainFragmentPagerAdapter;
import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.MessagesContainerFragment;
import com.gnusl.wow.Fragments.MessagesFragment;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Styles.CustomTypefaceSpan;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.gnusl.wow.Views.FontedTextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PagerDelegate, SmartTabLayout.TabProvider {

    private DrawerLayout drawer;
    private ViewPager viewPager;
    private MainFragmentPagerAdapter pagerAdapter;
    private SmartTabLayout viewPagerTab;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        // init views variables
        findViews();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // set custom font for navigation drawer items
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        // init header navigation
        initializeHeaderNavigation(navigationView.getHeaderView(0));

        // init pager with bottom tabs
        setUpViewPager();

        // set default tab
        setFragmentView(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // message section logic
            if (getmCurrentFragment() instanceof MessagesContainerFragment) {

                if (!(((MessagesContainerFragment) getmCurrentFragment()).getmCurrentFragment() instanceof MessagesFragment))
                    ((MessagesContainerFragment) getmCurrentFragment()).replaceFragment(FragmentTags.MessagesFragment);

            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.moment) {

            setFragmentView(1);

        } else if (id == R.id.messageSection) {

            setFragmentView(2);

        } else if (id == R.id.aristocracy) {

            startActivity(new Intent(MainActivity.this, AristocracyActivity.class));

        } else if (id == R.id.recharge) {

            startActivity(new Intent(MainActivity.this, RechargeActivity.class));

        } else if (id == R.id.earn_gold) {

            startActivity(new Intent(MainActivity.this, EarnGoldActivity.class));

        } else if (id == R.id.store) {

            startActivity(new Intent(MainActivity.this, StoreActivity.class));

        } else if (id == R.id.settings) {

            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/SFUIText-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, mNewTitle.length(), 0); Use this if you want to center the items
        mi.setTitle(mNewTitle);
    }


    private void findViews() {

        drawer = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.pager_container);
        viewPagerTab = findViewById(R.id.bottom_bar);

    }


    private void setUpViewPager() {

        pagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);

        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

        // listener on tab clecked
        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {

                // set fragment view
                setFragmentView(position);

            }
        });

        viewPager.setOffscreenPageLimit(3);

    }

    private void setFragmentView(int position) {

        if (viewPager != null)
            viewPager.setCurrentItem(position);

        // set tab colored
        View view = viewPagerTab.getTabAt(position);
        view.findViewById(R.id.root_view).setBackgroundColor(getResources().getColor(R.color.white));
        ((FontedTextView) view.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.active_color));
        ((AppCompatImageView) view.findViewById(R.id.icon)).setColorFilter(getResources().getColor(R.color.active_color));

        // set default for all tabs
        for (int i = 0; i < pagerAdapter.getCount(); i++)
            if (i != position) {

                View defaultView = viewPagerTab.getTabAt(i);
                defaultView.findViewById(R.id.root_view).setBackgroundColor(Color.TRANSPARENT);
                ((FontedTextView) defaultView.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
                ((AppCompatImageView) defaultView.findViewById(R.id.icon)).setColorFilter(Color.WHITE);
            }

    }

    private void initializeHeaderNavigation(View navigationView) {

        // go to profile
        navigationView.findViewById(R.id.imageView).setOnClickListener(v -> startActivity(new Intent(this, PrivateUserInfoActivity.class)));

        // set user info
        setUserInformation(navigationView);
    }

    private void setUserInformation(View headerView) {

        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // name
            ((TextView) headerView.findViewById(R.id.name_text)).setText(user.getName());

            // Id
            ((TextView) headerView.findViewById(R.id.id_text)).setText(String.valueOf("LV" + user.getLevel() + " " + "ID:" + user.getId()));

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(this)
                        .load(user.getImage_url())
                        .into(((ImageView) headerView.findViewById(R.id.imageView)));
        }
    }

    @Override
    public void onReplaceFragment(Fragment fragment, int position) {

        setmCurrentFragment(fragment);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.custom_bottom_tab_item_view, container, false);

        AppCompatImageView imageView = inflatedView.findViewById(R.id.icon);
        FontedTextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_room_unactive));
                fontedTextView.setText(getString(R.string.room));
                break;

            case 1:

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_moments_unactive));
                fontedTextView.setText(getString(R.string.moments));
                break;

            case 2:

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_message_unactive));
                fontedTextView.setText(getString(R.string.message));
                break;
        }

        return inflatedView;
    }

    // getters and setters region

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    public void setmCurrentFragment(Fragment mCurrentFragment) {
        this.mCurrentFragment = mCurrentFragment;
    }

    // endregion
}
