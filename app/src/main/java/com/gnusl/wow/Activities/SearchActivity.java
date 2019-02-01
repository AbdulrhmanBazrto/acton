package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gnusl.wow.Adapters.SearchFragmentPagerAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Delegates.SearchDelegate;
import com.gnusl.wow.Fragments.SearchRoomFragment;
import com.gnusl.wow.Fragments.UsersFragment;
import com.gnusl.wow.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class SearchActivity extends AppCompatActivity implements SmartTabLayout.TabProvider, PagerDelegate {

    public static final String SEARCH_FOR_USERS = "search-for-users";
    public static final String SEARCH_FOR_ROOMS = "search-for-rooms";

    private EditText searchEdittext;
    private ViewPager viewPager;
    private SearchDelegate searchRoomDelegate;
    private SearchDelegate searchUsersDelegate;

    SearchFragmentPagerAdapter searchFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // back click
        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        // initialize tabs pager
        initializeSmartTabs();

        // hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // init filter
        initializeFiltersByViews();

        if (getIntent().hasExtra(SEARCH_FOR_USERS)) {

            viewPager.setCurrentItem(1);

        } else { // search for rooms

            viewPager.setCurrentItem(0);
        }

    }

    private void initializeFiltersByViews() {

        findViewById(R.id.search_text_view).setOnClickListener(v -> filterRequest());
        findViewById(R.id.search_icon).setOnClickListener(v -> filterRequest());

        searchEdittext = findViewById(R.id.search_edit_text);
        searchEdittext.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                // send request
                filterRequest();
                return true;
            } else {
                return false;
            }
        });

    }

    private void initializeSmartTabs() {

        searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(this, getSupportFragmentManager(), this);

        searchFragmentPagerAdapter.setSearchRoomFragment(SearchRoomFragment.newInstance());
        searchFragmentPagerAdapter.setUsersFragment(UsersFragment.newInstance());

        searchRoomDelegate = searchFragmentPagerAdapter.getSearchRoomFragment();
        searchUsersDelegate= searchFragmentPagerAdapter.getUsersFragment();

        viewPager = findViewById(R.id.search_view_pager);
        viewPager.setAdapter(searchFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.search_smart_tab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

    }

    private void filterRequest() {

        if (searchEdittext.getText().toString().isEmpty())
            Toast.makeText(this, "you must have something to filter", Toast.LENGTH_SHORT).show();

        else if (viewPager.getCurrentItem() == 1) {

            searchUsersDelegate.onSearchByTag(searchEdittext.getText().toString());

        } else {

            searchRoomDelegate.onSearchByTag(searchEdittext.getText().toString());
        }

    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = inflatedView.findViewById(R.id.title);

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

    @Override
    public void onReplaceFragment(Fragment fragment, int position) {

    }
}
