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
    private ProgressDialog progressDialog;

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

        viewPager = (ViewPager) findViewById(R.id.pager_container);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.top_tab_bar);

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
        this.progressDialog = ProgressDialog.show(this, "", "loading data..");

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

        FontedTextView fontedTextView = (FontedTextView) inflatedView.findViewById(R.id.title);

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

        if (progressDialog != null)
            progressDialog.dismiss();

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

    private void test(){

        try {
            JSONObject jsonObject=new JSONObject("{\n" +
                    "        \"channel\": {\n" +
                    "            \"today\": [],\n" +
                    "            \"week\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"66\",\n" +
                    "                    \"channel_id\": 1,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"name\": \"0\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 1,\n" +
                    "                        \"password\": \"simon\",\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 0,\n" +
                    "                        \"subscription_price\": 500,\n" +
                    "                        \"background_path\": \"1545691653517785931.jpg\",\n" +
                    "                        \"priority\": 1,\n" +
                    "                        \"created_at\": \"2018-12-11 18:50:36\",\n" +
                    "                        \"updated_at\": \"2018-12-24 22:47:34\",\n" +
                    "                        \"country_code\": \"us\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 1,\n" +
                    "                        \"lock_type_id\": null,\n" +
                    "                        \"tag\": null,\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/1545691653517785931.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/us/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"66\",\n" +
                    "                    \"channel_id\": 3,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 3,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 0,\n" +
                    "                        \"password\": null,\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 1,\n" +
                    "                        \"subscription_price\": null,\n" +
                    "                        \"background_path\": \"default_bg.jpg\",\n" +
                    "                        \"priority\": null,\n" +
                    "                        \"created_at\": \"2018-12-30 22:46:45\",\n" +
                    "                        \"updated_at\": \"2018-12-30 22:46:45\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 1,\n" +
                    "                        \"lock_type_id\": null,\n" +
                    "                        \"tag\": null,\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/default_bg.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"44\",\n" +
                    "                    \"channel_id\": 2,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"momo\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 1,\n" +
                    "                        \"password\": \"simon\",\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 0,\n" +
                    "                        \"subscription_price\": 500,\n" +
                    "                        \"background_path\": \"default.jpg\",\n" +
                    "                        \"priority\": 1,\n" +
                    "                        \"created_at\": \"2018-12-16 19:28:05\",\n" +
                    "                        \"updated_at\": \"2018-12-25 16:48:13\",\n" +
                    "                        \"country_code\": \"us\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 2,\n" +
                    "                        \"lock_type_id\": 1,\n" +
                    "                        \"tag\": \"sport\",\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/us/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"month\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"66\",\n" +
                    "                    \"channel_id\": 1,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"name\": \"0\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 1,\n" +
                    "                        \"password\": \"simon\",\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 0,\n" +
                    "                        \"subscription_price\": 500,\n" +
                    "                        \"background_path\": \"1545691653517785931.jpg\",\n" +
                    "                        \"priority\": 1,\n" +
                    "                        \"created_at\": \"2018-12-11 18:50:36\",\n" +
                    "                        \"updated_at\": \"2018-12-24 22:47:34\",\n" +
                    "                        \"country_code\": \"us\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 1,\n" +
                    "                        \"lock_type_id\": null,\n" +
                    "                        \"tag\": null,\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/1545691653517785931.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/us/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"66\",\n" +
                    "                    \"channel_id\": 3,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 3,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 0,\n" +
                    "                        \"password\": null,\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 1,\n" +
                    "                        \"subscription_price\": null,\n" +
                    "                        \"background_path\": \"default_bg.jpg\",\n" +
                    "                        \"priority\": null,\n" +
                    "                        \"created_at\": \"2018-12-30 22:46:45\",\n" +
                    "                        \"updated_at\": \"2018-12-30 22:46:45\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 1,\n" +
                    "                        \"lock_type_id\": null,\n" +
                    "                        \"tag\": null,\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/default_bg.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"44\",\n" +
                    "                    \"channel_id\": 2,\n" +
                    "                    \"channel\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"momo\",\n" +
                    "                        \"is_active\": 0,\n" +
                    "                        \"has_password\": 1,\n" +
                    "                        \"password\": \"simon\",\n" +
                    "                        \"user_id\": 2,\n" +
                    "                        \"is_free\": 0,\n" +
                    "                        \"subscription_price\": 500,\n" +
                    "                        \"background_path\": \"default.jpg\",\n" +
                    "                        \"priority\": 1,\n" +
                    "                        \"created_at\": \"2018-12-16 19:28:05\",\n" +
                    "                        \"updated_at\": \"2018-12-25 16:48:13\",\n" +
                    "                        \"country_code\": \"us\",\n" +
                    "                        \"num_users\": 0,\n" +
                    "                        \"description\": null,\n" +
                    "                        \"channel_type_id\": 2,\n" +
                    "                        \"lock_type_id\": 1,\n" +
                    "                        \"tag\": \"sport\",\n" +
                    "                        \"background_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/us/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        \"sent\": {\n" +
                    "            \"today\": [],\n" +
                    "            \"week\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"154\",\n" +
                    "                    \"user_id_from\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"mobile\": null,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": null,\n" +
                    "                        \"last_login\": \"2018-11-13 00:42:59\",\n" +
                    "                        \"lon\": null,\n" +
                    "                        \"lat\": null,\n" +
                    "                        \"image\": \"default.jpg\",\n" +
                    "                        \"social_id\": null,\n" +
                    "                        \"social_type\": null,\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": null,\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"22\",\n" +
                    "                    \"user_id_from\": {\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad123@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"mobile\": 1223321,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": \"123321\",\n" +
                    "                        \"last_login\": \"2018-11-13 00:56:41\",\n" +
                    "                        \"lon\": 45,\n" +
                    "                        \"lat\": 35,\n" +
                    "                        \"image\": \"first.jpg\",\n" +
                    "                        \"social_id\": \"1234123\",\n" +
                    "                        \"social_type\": \"facebook\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": \"{'hide_friends':true}\",\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/first.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"month\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"154\",\n" +
                    "                    \"user_id_from\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"mobile\": null,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": null,\n" +
                    "                        \"last_login\": \"2018-11-13 00:42:59\",\n" +
                    "                        \"lon\": null,\n" +
                    "                        \"lat\": null,\n" +
                    "                        \"image\": \"default.jpg\",\n" +
                    "                        \"social_id\": null,\n" +
                    "                        \"social_type\": null,\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": null,\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"22\",\n" +
                    "                    \"user_id_from\": {\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad123@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"mobile\": 1223321,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": \"123321\",\n" +
                    "                        \"last_login\": \"2018-11-13 00:56:41\",\n" +
                    "                        \"lon\": 45,\n" +
                    "                        \"lat\": 35,\n" +
                    "                        \"image\": \"first.jpg\",\n" +
                    "                        \"social_id\": \"1234123\",\n" +
                    "                        \"social_type\": \"facebook\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": \"{'hide_friends':true}\",\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/first.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        \"recieved\": {\n" +
                    "            \"today\": [],\n" +
                    "            \"week\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"154\",\n" +
                    "                    \"user_id_to\": {\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad123@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"mobile\": 1223321,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": \"123321\",\n" +
                    "                        \"last_login\": \"2018-11-13 00:56:41\",\n" +
                    "                        \"lon\": 45,\n" +
                    "                        \"lat\": 35,\n" +
                    "                        \"image\": \"first.jpg\",\n" +
                    "                        \"social_id\": \"1234123\",\n" +
                    "                        \"social_type\": \"facebook\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": \"{'hide_friends':true}\",\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/first.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"22\",\n" +
                    "                    \"user_id_to\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"mobile\": null,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": null,\n" +
                    "                        \"last_login\": \"2018-11-13 00:42:59\",\n" +
                    "                        \"lon\": null,\n" +
                    "                        \"lat\": null,\n" +
                    "                        \"image\": \"default.jpg\",\n" +
                    "                        \"social_id\": null,\n" +
                    "                        \"social_type\": null,\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": null,\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ],\n" +
                    "            \"month\": [\n" +
                    "                {\n" +
                    "                    \"total\": \"154\",\n" +
                    "                    \"user_id_to\": {\n" +
                    "                        \"id\": 6,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad123@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:56:41\",\n" +
                    "                        \"mobile\": 1223321,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": \"123321\",\n" +
                    "                        \"last_login\": \"2018-11-13 00:56:41\",\n" +
                    "                        \"lon\": 45,\n" +
                    "                        \"lat\": 35,\n" +
                    "                        \"image\": \"first.jpg\",\n" +
                    "                        \"social_id\": \"1234123\",\n" +
                    "                        \"social_type\": \"facebook\",\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": \"{'hide_friends':true}\",\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/first.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    \"total\": \"22\",\n" +
                    "                    \"user_id_to\": {\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"name\": \"mohammad\",\n" +
                    "                        \"email\": \"mohammad@gmail.com\",\n" +
                    "                        \"email_verified_at\": null,\n" +
                    "                        \"created_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"updated_at\": \"2018-11-12 22:42:59\",\n" +
                    "                        \"mobile\": null,\n" +
                    "                        \"type\": \"user\",\n" +
                    "                        \"ip_address\": null,\n" +
                    "                        \"fcm_token\": null,\n" +
                    "                        \"last_login\": \"2018-11-13 00:42:59\",\n" +
                    "                        \"lon\": null,\n" +
                    "                        \"lat\": null,\n" +
                    "                        \"image\": \"default.jpg\",\n" +
                    "                        \"social_id\": null,\n" +
                    "                        \"social_type\": null,\n" +
                    "                        \"country_code\": \"sa\",\n" +
                    "                        \"privacy\": null,\n" +
                    "                        \"language\": \"ar\",\n" +
                    "                        \"gender\": \"male\",\n" +
                    "                        \"balance\": 0,\n" +
                    "                        \"image_url\": \"http://fat7al.com/wow/public/uploads/default.jpg\",\n" +
                    "                        \"country_code_url\": \"https://www.countryflags.io/sa/flat/64.png\"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    }");


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

            // send event
            EventBus.getDefault().post(new RefreshGiftsDelegate());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
