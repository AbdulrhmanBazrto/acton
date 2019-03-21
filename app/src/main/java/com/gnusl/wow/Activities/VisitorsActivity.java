package com.gnusl.wow.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.FollowingRecyclerViewAdapter;
import com.gnusl.wow.Adapters.VisitorsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Models.Visit;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class VisitorsActivity extends AppCompatActivity implements ConnectionDelegate {

    VisitorsRecyclerViewAdapter visitorsRecyclerViewAdapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // initialize adapter
        initializeAdapter();

        // send request
        sendFollowersRequest();
    }

    private void initializeAdapter() {

        RecyclerView recyclerView = findViewById(R.id.visitor_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        visitorsRecyclerViewAdapter = new VisitorsRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(visitorsRecyclerViewAdapter);
    }

    private void sendFollowersRequest() {

        // make progress dialog
        LoaderPopUp.show(this);

        // send request
        if (SharedPreferencesUtils.getUser() != null)
            APIConnectionNetwork.GetVisitors(SharedPreferencesUtils.getUser().getId(), this);
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

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<Visit> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            users.add(Visit.newInstance(jsonArray.optJSONObject(i)));
        }

        // notify
        visitorsRecyclerViewAdapter.setVisitors(users);
        visitorsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        LoaderPopUp.dismissLoader();
    }
}
