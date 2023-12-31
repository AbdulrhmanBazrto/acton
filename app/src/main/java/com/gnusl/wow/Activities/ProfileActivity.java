package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Adapters.GiftTypesFragmentPagerAdapter;
import com.gnusl.wow.Adapters.ProfileFragmentPagerAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Fragments.PersonalInfoProfileFragment;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Views.FontedTextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_SHORT;

public class ProfileActivity extends AppCompatActivity implements PagerDelegate, SmartTabLayout.TabProvider, ConnectionDelegate {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    public final static String USER_ID = "user_id";
    private ProfileFragmentPagerAdapter profileFragmentPagerAdapter;
    private SmartTabLayout viewPagerTab;
    private ViewPager viewPager;
    private Fragment mCurrentFragment;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!getIntent().hasExtra(USER_ID)) {

            finish();
            return;
        }

        // init views variables
        findViews();

        // init pager with bottom tabs
        setUpViewPager();

        // set default tab
        setFragmentView(0);

        // get user details
        sendUserDetailsRequest();
    }

    private void findViews() {

        viewPager = findViewById(R.id.pager_container);
        viewPagerTab = findViewById(R.id.top_tab_bar);

        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.messages_button).setOnClickListener(v ->goToMessagesConversationActivity());
        findViewById(R.id.follow_button).setOnClickListener(v->sendFollowedUser());
    }

    private void setUpViewPager() {

        profileFragmentPagerAdapter = new ProfileFragmentPagerAdapter(this, getSupportFragmentManager(), this);
        viewPager.setAdapter(profileFragmentPagerAdapter);

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
        //view.findViewById(R.id.root_view).setBackgroundColor(getResources().getColor(R.color.white));
        ((FontedTextView) view.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.black));

        // set default for all tabs
        for (int i = 0; i < profileFragmentPagerAdapter.getCount(); i++)
            if (i != position) {

                View defaultView = viewPagerTab.getTabAt(i);
                //defaultView.findViewById(R.id.root_view).setBackgroundColor(Color.TRANSPARENT);
                ((FontedTextView) defaultView.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.no_moments_label_text_color));
            }

    }

    private void sendUserDetailsRequest() {

        // make progress dialog
        LoaderPopUp.show(this);

        // send request
        APIConnectionNetwork.GetUserDetails(getUserId(), this);
    }

    private void goToMessagesConversationActivity(){

        Intent intent = new Intent(this, MessagesConversationActivity.class);
        intent.putExtra(MessagesConversationActivity.USER_ID, getUserId());
        startActivity(intent);
    }

    private void sendFollowedUser(){

        if(getUser().isFollowed()) {
            getUser().setFollowed(false);
            ((ImageView) findViewById(R.id.follow_icon)).setImageResource(R.drawable.ic_action_heart_unactive);
        }else {
            getUser().setFollowed(true);
            ((ImageView) findViewById(R.id.follow_icon)).setImageResource(R.drawable.ic_action_heart_active);
        }

        // send request
        APIConnectionNetwork.SendFollowToUser(getUserId(),this);
    }

    @Override
    public void onReplaceFragment(Fragment fragment, int position) {

        this.mCurrentFragment=fragment;

        setFragmentView(position);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.custom_tab_public_profile_item_view, container, false);

        FontedTextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:

                fontedTextView.setText(getString(R.string.the_personal_info));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.the_path));
                break;

        }

        return inflatedView;
    }

    private void refreshUserDetails() {

        if (getUser() == null)
            return;

        // load image
        if (getUser().getImage_url() != null && !user.getImage_url().isEmpty())
            Glide.with(this)
                    .load(getUser().getImage_url())
                    .into((ImageView) findViewById(R.id.user_image));

        // name
        ((TextView) findViewById(R.id.user_name)).setText(getUser().getName());

        // id
        ((TextView) findViewById(R.id.user_Id)).setText(String.valueOf("ID: " + getUser().getId()));

        // progress & level
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(getUser().getLevel()*10);
        ((TextView) findViewById(R.id.percent_text)).setText(String.valueOf((getUser().getLevel()*10)+"/100"));
        ((TextView) findViewById(R.id.level_2)).setText(String.valueOf("Lv"+getUser().getLevel()));

        // followed
        ((ImageView) findViewById(R.id.follow_icon)).setImageResource(getUser().isFollowed()?R.drawable.ic_action_heart_active:R.drawable.ic_action_heart_unactive);

        // personal info
        if(mCurrentFragment instanceof PersonalInfoProfileFragment){

            ((PersonalInfoProfileFragment) mCurrentFragment).getCountryCodeTv().setText(getUser().getCountryCode());
        }
    }


    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, " Connection Failure", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        // parse user
        if (jsonObject.has("user")) {

            try {
                User user = User.newInstance(jsonObject.getJSONObject("user"));

                setUser(user);

                // refresh
                refreshUserDetails();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    public int getUserId() {

        return getIntent().getIntExtra(USER_ID, 0);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
