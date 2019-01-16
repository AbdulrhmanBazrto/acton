package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Adapters.SearchFragmentPagerAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Delegates.SearchByUsersDelegate;
import com.gnusl.wow.Fragments.UsersFragment;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.KeyboardUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchActivity extends AppCompatActivity implements SmartTabLayout.TabProvider, ConnectionDelegate, PagerDelegate {

    private EditText searchEdittext;
    private ProgressDialog progressDialog;
    private SearchByUsersDelegate searchByUsersDelegate;

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

        SearchFragmentPagerAdapter searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(this, getSupportFragmentManager(), this);

        ViewPager viewPager = findViewById(R.id.search_view_pager);
        viewPager.setAdapter(searchFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.search_smart_tab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

    }

    private void filterRequest() {

        if (searchEdittext.getText().toString().isEmpty())
            Toast.makeText(this, "you must have name for user", Toast.LENGTH_SHORT).show();

        else {
            // make progress dialog
            this.progressDialog = ProgressDialog.show(this, "", "search for users..");

            // upload image
            APIConnectionNetwork.SearchForUsers(searchEdittext.getText().toString(), this);

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
    public void onConnectionFailure() {

        Toast.makeText(this, "Connection Failure", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<User> users = User.parseJSONArray(jsonArray);

        // call delegate for refresh
        if (searchByUsersDelegate != null)
            searchByUsersDelegate.onSearchResultDone(users);

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onReplaceFragment(Fragment fragment, int position) {

        if(searchByUsersDelegate==null && fragment instanceof UsersFragment)
            searchByUsersDelegate= (SearchByUsersDelegate) fragment;
    }
}
