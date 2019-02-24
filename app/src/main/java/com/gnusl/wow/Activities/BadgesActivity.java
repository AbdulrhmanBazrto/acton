package com.gnusl.wow.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.AllBadgesRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MicUsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MyBadgesRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BadgesActivity extends AppCompatActivity {

    RecyclerView rvMyBadges;
    MyBadgesRecyclerViewAdapter myBadgesRecyclerViewAdapter;

    RecyclerView rvAllBadges;
    AllBadgesRecyclerViewAdapter allBadgesRecyclerViewAdapter;
    ArrayList<Badge> myBadges = new ArrayList<>();

    TabLayout badgeTabLayout;

    int queryType = 0; // 0 honor 1 achievement

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        rvMyBadges = findViewById(R.id.rv_my_badges);
        rvAllBadges = findViewById(R.id.rv_badges);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvMyBadges.setLayoutManager(linearLayoutManager);
        myBadgesRecyclerViewAdapter = new MyBadgesRecyclerViewAdapter(this, new ArrayList<>());
        rvMyBadges.setAdapter(myBadgesRecyclerViewAdapter);
        getMyBadges();


        badgeTabLayout = findViewById(R.id.tl_badges_type);
        badgeTabLayout.addTab(badgeTabLayout.newTab().setContentDescription("bb").setText("شارات الانجازات"));
        badgeTabLayout.addTab(badgeTabLayout.newTab().setContentDescription("aa").setText("شارات الشرف"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvAllBadges.setLayoutManager(gridLayoutManager);
        allBadgesRecyclerViewAdapter = new AllBadgesRecyclerViewAdapter(this, new ArrayList<>());
        rvAllBadges.setAdapter(allBadgesRecyclerViewAdapter);

        badgeTabLayout.getTabAt(1).select();

        badgeTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    queryType = 1;
                } else if (tab.getPosition() == 1) {
                    queryType = 0;
                }
                LoaderPopUp.show(BadgesActivity.this);
                getBadges();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getBadges() {
        APIConnectionNetwork.GetAllBadges(new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionError(ANError anError) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(String response) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
                ArrayList<Badge> badges = Badge.parseJSONArray(jsonArray);
                ArrayList<Badge> honorBadges = new ArrayList<>();
                ArrayList<Badge> achBadges = new ArrayList<>();
                for (int i = 0; i < badges.size(); i++) {
                    if (badges.get(i).getType().equalsIgnoreCase("honour")) {
                        honorBadges.add(badges.get(i));
                    } else {
                        achBadges.add(badges.get(i));
                    }
                }

                for (int i = 0; i < myBadges.size(); i++) {
                    if (queryType == 0) {
                        for (int j = 0; j < honorBadges.size(); j++) {
                            if (myBadges.get(i).getId() == honorBadges.get(j).getId()) {
                                honorBadges.get(j).setGranted(true);
                            }
                        }
                    } else {
                        for (int j = 0; j < achBadges.size(); j++) {
                            if (myBadges.get(i).getId() == achBadges.get(j).getId()) {
                                achBadges.get(j).setGranted(true);
                            }
                        }
                    }
                }

                if (queryType == 0) {
                    allBadgesRecyclerViewAdapter.setUsers(honorBadges);
                } else {
                    allBadgesRecyclerViewAdapter.setUsers(achBadges);
                }
            }
        });
    }

    private void getMyBadges() {
        APIConnectionNetwork.GetMyBadges(new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionError(ANError anError) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(String response) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
                myBadges = Badge.parseJSONArray(jsonArray);
                for (int i = 0; i < myBadges.size(); i++) {
                    myBadges.get(i).setGranted(true);
                }
                myBadgesRecyclerViewAdapter.setUsers(myBadges);
                getBadges();
            }
        });
    }
}
