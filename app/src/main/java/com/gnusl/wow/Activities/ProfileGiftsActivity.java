package com.gnusl.wow.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.GiftsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class ProfileGiftsActivity extends AppCompatActivity implements ConnectionDelegate {

    GiftsRecyclerViewAdapter giftsRecyclerViewAdapter;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_gifts);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // initialize adapter
        initializeAdapter();

        // send request
        sendGiftsRequest();
    }

    private void initializeAdapter() {

        RecyclerView recyclerView = findViewById(R.id.gifts_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        giftsRecyclerViewAdapter = new GiftsRecyclerViewAdapter(this, new ArrayList<>(), null, null, null, null);
        recyclerView.setAdapter(giftsRecyclerViewAdapter);
    }

    private void sendGiftsRequest() {

        // make progress dialog
        LoaderPopUp.show(this);

        // send request
        APIConnectionNetwork.GetProfileGifts(this);
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
        ArrayList<Gift> gifts = Gift.parseJSONArray(jsonArray);

        // notify
        giftsRecyclerViewAdapter.setGifts(gifts);
        giftsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        LoaderPopUp.dismissLoader();
    }
}
