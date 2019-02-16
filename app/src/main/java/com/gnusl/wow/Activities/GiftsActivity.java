package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.GiftTypesFragmentPagerAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.PagerDelegate;
import com.gnusl.wow.Models.GiftRoomRank;
import com.gnusl.wow.Models.GiftUserRank;
import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.FontedTextView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class GiftsActivity extends AppCompatActivity implements PagerDelegate, SmartTabLayout.TabProvider, ConnectionDelegate {

    private GiftTypesFragmentPagerAdapter pagerAdapter;
    private SmartTabLayout viewPagerTab;
    private ViewPager viewPager;
    private Fragment mCurrentFragment;

    // received data
    private ArrayList<GiftUserRank> todayReceivedUserRanks;
    private ArrayList<GiftUserRank> weekReceivedUserRanks;
    private ArrayList<GiftUserRank> montReceivedhUserRanks;

    // sent data
    private ArrayList<GiftUserRank> todaySentUserRanks;
    private ArrayList<GiftUserRank> weekSentUserRanks;
    private ArrayList<GiftUserRank> montSenthUserRanks;

    // channel data
    private ArrayList<GiftRoomRank> todayRoomRanks;
    private ArrayList<GiftRoomRank> weekRoomRanks;
    private ArrayList<GiftRoomRank> monthRoomRanks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        // init views variables
        findViews();

        // init pager with bottom tabs
        setUpViewPager();

        // set default tab
        setFragmentView(0);

        // get gifts
        sendGiftsRequest();
    }

    private void findViews() {

        viewPager = findViewById(R.id.pager_container);
        viewPagerTab = findViewById(R.id.top_tab_bar);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());
    }

    private void setUpViewPager() {

        pagerAdapter = new GiftTypesFragmentPagerAdapter(this, getSupportFragmentManager(), this);
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

        // set default for all tabs
        for (int i = 0; i < pagerAdapter.getCount(); i++)
            if (i != position) {

                View defaultView = viewPagerTab.getTabAt(i);
                defaultView.findViewById(R.id.root_view).setBackgroundColor(Color.TRANSPARENT);
                ((FontedTextView) defaultView.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
            }

    }

    private void sendGiftsRequest() {

        // make progress dialog
        LoaderPopUp.show(this);

        // send request
        APIConnectionNetwork.GetExploreGifts(this);
    }


    @Override
    public void onReplaceFragment(Fragment fragment, int position) {

    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(this);

        View inflatedView = inflater.inflate(R.layout.custom_top_tab_gifts_item_view, container, false);

        FontedTextView fontedTextView = inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:

               // inflatedView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_room_unactive));
                fontedTextView.setText(getString(R.string.recieved_gifts));
                break;

            case 1:

               // imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_moments_unactive));
                fontedTextView.setText(getString(R.string.sent_gifts));
                break;

            case 2:

               // imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_message_unactive));
                fontedTextView.setText(getString(R.string.room_gifts));
                break;
        }

        return inflatedView;
    }

    private void parseChannelsData(JSONObject jsonData){

        if(jsonData.has("today")){

            try {
                setTodayRoomRanks(GiftRoomRank.parseJSONArray(jsonData.getJSONArray("today")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("week")){

            try {
                setWeekRoomRanks(GiftRoomRank.parseJSONArray(jsonData.getJSONArray("week")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("month")){

            try {
                setMonthRoomRanks(GiftRoomRank.parseJSONArray(jsonData.getJSONArray("month")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseSentData(JSONObject jsonData){

        if(jsonData.has("today")){

            try {
                setTodaySentUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("today")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("week")){

            try {
                setWeekSentUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("week")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("month")){

            try {
                setMontSenthUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("month")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseReceivedData(JSONObject jsonData){

        if(jsonData.has("today")){

            try {
                setTodayReceivedUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("today")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("week")){

            try {
                setWeekReceivedUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("week")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonData.has("month")){

            try {
                setMontReceivedhUserRanks(GiftUserRank.parseJSONArray(jsonData.getJSONArray("month")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

        // parsing
        if(jsonObject.has("channel")){

            try {
                parseChannelsData(jsonObject.getJSONObject("channel"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("sent")){

            try {
                parseSentData(jsonObject.getJSONObject("sent"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonObject.has("recieved")){

            try {
                parseReceivedData(jsonObject.getJSONObject("recieved"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LoaderPopUp.dismissLoader();

        // send event
        EventBus.getDefault().post(new RefreshGiftsDelegate());
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    public ArrayList<GiftUserRank> getTodayReceivedUserRanks() {
        return todayReceivedUserRanks;
    }

    public void setTodayReceivedUserRanks(ArrayList<GiftUserRank> todayReceivedUserRanks) {
        this.todayReceivedUserRanks = todayReceivedUserRanks;
    }

    public ArrayList<GiftUserRank> getWeekReceivedUserRanks() {
        return weekReceivedUserRanks;
    }

    public void setWeekReceivedUserRanks(ArrayList<GiftUserRank> weekReceivedUserRanks) {
        this.weekReceivedUserRanks = weekReceivedUserRanks;
    }

    public ArrayList<GiftUserRank> getMontReceivedhUserRanks() {
        return montReceivedhUserRanks;
    }

    public void setMontReceivedhUserRanks(ArrayList<GiftUserRank> montReceivedhUserRanks) {
        this.montReceivedhUserRanks = montReceivedhUserRanks;
    }

    public ArrayList<GiftUserRank> getTodaySentUserRanks() {
        return todaySentUserRanks;
    }

    public void setTodaySentUserRanks(ArrayList<GiftUserRank> todaySentUserRanks) {
        this.todaySentUserRanks = todaySentUserRanks;
    }

    public ArrayList<GiftUserRank> getWeekSentUserRanks() {
        return weekSentUserRanks;
    }

    public void setWeekSentUserRanks(ArrayList<GiftUserRank> weekSentUserRanks) {
        this.weekSentUserRanks = weekSentUserRanks;
    }

    public ArrayList<GiftUserRank> getMontSenthUserRanks() {
        return montSenthUserRanks;
    }

    public void setMontSenthUserRanks(ArrayList<GiftUserRank> montSenthUserRanks) {
        this.montSenthUserRanks = montSenthUserRanks;
    }

    public ArrayList<GiftRoomRank> getTodayRoomRanks() {
        return todayRoomRanks;
    }

    public void setTodayRoomRanks(ArrayList<GiftRoomRank> todayRoomRanks) {
        this.todayRoomRanks = todayRoomRanks;
    }

    public ArrayList<GiftRoomRank> getWeekRoomRanks() {
        return weekRoomRanks;
    }

    public void setWeekRoomRanks(ArrayList<GiftRoomRank> weekRoomRanks) {
        this.weekRoomRanks = weekRoomRanks;
    }

    public ArrayList<GiftRoomRank> getMonthRoomRanks() {
        return monthRoomRanks;
    }

    public void setMonthRoomRanks(ArrayList<GiftRoomRank> monthRoomRanks) {
        this.monthRoomRanks = monthRoomRanks;
    }
}
