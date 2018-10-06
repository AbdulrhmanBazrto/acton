package com.gnusl.wow.Activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Adapters.SearchFragmentPagerAdapter;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.KeyboardUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class SearchActivity extends AppCompatActivity implements SmartTabLayout.TabProvider{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // back click
        findViewById(R.id.back_button).setOnClickListener(v->finish());

        // initialize tabs pager
        initializeSmartTabs();

        // hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initializeSmartTabs() {

        SearchFragmentPagerAdapter searchFragmentPagerAdapter= new SearchFragmentPagerAdapter(this, getSupportFragmentManager(), null);

        ViewPager viewPager = (ViewPager) findViewById(R.id.search_view_pager);
        viewPager.setAdapter(searchFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.search_smart_tab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = (TextView) inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:
                fontedTextView.setText(getString(R.string.rooms));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.users));
                break;


        }

        return inflatedView;
    }
}
