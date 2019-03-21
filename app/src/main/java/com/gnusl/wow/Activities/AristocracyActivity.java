package com.gnusl.wow.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.AristocracyRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Aristocracy;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AristocracyActivity extends AppCompatActivity {

    RecyclerView rvAllBadges;
    AristocracyRecyclerViewAdapter aristocracyRecyclerViewAdapter;
    ArrayList<Aristocracy> aristocracies = new ArrayList<>();

    TabLayout badgeTabLayout;

    ImageView ivAristocracy, ivUserImage;
    TextView tvAristocracy, tvUserArst, tvSubprice;

    Button btnSub;

    Aristocracy currentSelectedAristocracy = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aristocracy);
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        rvAllBadges = findViewById(R.id.rv_badges);
        ivAristocracy = findViewById(R.id.iv_aristocracy);
        tvAristocracy = findViewById(R.id.tv_aristocracy);
        tvUserArst = findViewById(R.id.tv_user_arst);
        ivUserImage = findViewById(R.id.iv_user_image);

        tvSubprice = findViewById(R.id.tv_sub_price);
        btnSub = findViewById(R.id.btn_sub);

        Picasso.with(this).load(SharedPreferencesUtils.getUser().getImage_url()).into(ivUserImage);

        badgeTabLayout = findViewById(R.id.tl_badges_type);
        badgeTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        badgeTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvAllBadges.setLayoutManager(gridLayoutManager);
        aristocracyRecyclerViewAdapter = new AristocracyRecyclerViewAdapter(this, new ArrayList<>());
        rvAllBadges.setAdapter(aristocracyRecyclerViewAdapter);


        badgeTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                for (Aristocracy ar : aristocracies) {
                    if (ar.getId() == Integer.parseInt(tab.getContentDescription().toString())) {
                        aristocracyRecyclerViewAdapter.setAristocracies(ar.getAristocracyDetails());
                        tvAristocracy.setText(ar.getName());
                        Picasso.with(AristocracyActivity.this).load(ar.getImageUrl()).into(ivAristocracy);
                        tvSubprice.setText(String.valueOf(ar.getPrice()));
                        currentSelectedAristocracy = ar;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btnSub.setOnClickListener(v -> {
            subscripeToArst();
        });

        getAristocracy();
    }

    private void subscripeToArst() {
        if (currentSelectedAristocracy != null) {
            APIConnectionNetwork.SaveAristocracy(currentSelectedAristocracy.getId(), new ConnectionDelegate() {
                @Override
                public void onConnectionFailure() {

                }

                @Override
                public void onConnectionError(ANError anError) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(anError.getErrorBody());
                        if (jsonObject.has("payment_status")) {
                            if (jsonObject.optString("payment_status").equalsIgnoreCase("error")) {
                                startActivity(new Intent(AristocracyActivity.this, RechargeActivity.class));
                            }
                        }
                    } catch (JSONException e) {

                    }
                }

                @Override
                public void onConnectionSuccess(String response) {

                }

                @Override
                public void onConnectionSuccess(JSONObject jsonObject) {
                    Toast.makeText(AristocracyActivity.this, "done", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onConnectionSuccess(JSONArray jsonArray) {

                }
            });
        } else {
            Toast.makeText(this, "current is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAristocracy() {
        APIConnectionNetwork.GetAristocracy(new ConnectionDelegate() {
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
                aristocracies = Aristocracy.parseJSONArray(jsonArray);
                for (Aristocracy ar : aristocracies)
                    badgeTabLayout.addTab(badgeTabLayout.newTab().setContentDescription(String.valueOf(ar.getId())).setText(ar.getName()));
                if (aristocracies.size() > 0) {
                    aristocracyRecyclerViewAdapter.setAristocracies(aristocracies.get(0).getAristocracyDetails());
                    badgeTabLayout.getTabAt(aristocracies.size() - 1).select();
                }

                if (Aristocracy.currentAristocracy != null) {
                    tvUserArst.setText(Aristocracy.currentAristocracy.getName());
                } else {
                    tvUserArst.setText(getString(R.string.not_aris_yet));
                }
            }
        });
    }
}
