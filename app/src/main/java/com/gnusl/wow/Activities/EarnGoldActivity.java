package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.EarnGoldSectionAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.EarnGoldSection;
import com.gnusl.wow.Models.EarnGoldTask;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class EarnGoldActivity extends AppCompatActivity implements ConnectionDelegate {

    private EarnGoldSectionAdapter earnGoldSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_gold);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        // init adapter
        initializeAdapter();

        // send request
        sendEarnGoldDataRequest();

    }

    private void initializeAdapter(){

        RecyclerView recyclerView = findViewById(R.id.earn_gold_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        earnGoldSectionAdapter = new EarnGoldSectionAdapter(this);
        recyclerView.setAdapter(earnGoldSectionAdapter);
    }

    private void sendEarnGoldDataRequest() {

        // make progress dialog
        LoaderPopUp.show(this);

        // send request
        APIConnectionNetwork.GetEarnGoldInfo(this);
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
        EarnGoldSection earnGoldSection = new EarnGoldSection();
        earnGoldSection.setHeaderTitle(getString(R.string.daily_missions));
        earnGoldSection.setEarnGoldTasks(EarnGoldTask.parseJSONArray(jsonArray));

        ArrayList<EarnGoldSection> earnGoldSections = new ArrayList<>();
        earnGoldSections.add(earnGoldSection);

        // refresh
        earnGoldSectionAdapter.setEarnGoldSections(earnGoldSections);
        earnGoldSectionAdapter.notifyDataSetChanged();

        LoaderPopUp.dismissLoader();

    }
}


