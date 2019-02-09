package com.gnusl.wow.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.BadgesRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class ProfileBadgesActivity extends AppCompatActivity implements ConnectionDelegate {

    public final static String USER_ID = "user_id";

    private BadgesRecyclerViewAdapter badgesRecyclerViewAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_badges);

        if (!getIntent().hasExtra(USER_ID)) {

            finish();
            return;
        }


        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // init adapter
        initializeAdapter();

        // send request
        sendProfileBadgesDataRequest();

    }

    private void initializeAdapter() {

        RecyclerView recyclerView = findViewById(R.id.badges_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        badgesRecyclerViewAdapter = new BadgesRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(badgesRecyclerViewAdapter);
    }

    private void sendProfileBadgesDataRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(this, "", "loading..");

        // send request
        APIConnectionNetwork.GetProfileBadges(getUserId(),this);
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

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<Badge> badges=Badge.parseJSONArray(jsonArray);

        // refresh
        badgesRecyclerViewAdapter.setBadges(badges);
        badgesRecyclerViewAdapter.notifyDataSetChanged();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    public int getUserId() {

        return getIntent().getIntExtra(USER_ID, 0);
    }

    public static void launch(Activity activity,int userId) {

        Intent intent = new Intent(activity, ProfileBadgesActivity.class);
        intent.putExtra(USER_ID,userId);
        activity.startActivity(intent);
    }
}
