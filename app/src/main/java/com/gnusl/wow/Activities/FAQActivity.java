package com.gnusl.wow.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.FAQDetailsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.FAQTabsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ChooseFAQDelegate;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.FAQ;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity implements ConnectionDelegate, ChooseFAQDelegate {

    FAQTabsRecyclerViewAdapter faqTabsRecyclerViewAdapter;
    FAQDetailsRecyclerViewAdapter faqDetailsRecyclerViewAdapter;
    RecyclerView rvTabs, rvQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvTabs = findViewById(R.id.rv_tabs);
        rvQuestions = findViewById(R.id.rv_questions);

        rvTabs.setLayoutManager(new GridLayoutManager(this, 3));

        rvQuestions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        faqTabsRecyclerViewAdapter = new FAQTabsRecyclerViewAdapter(this, new ArrayList<>(), this::onSelectFAQ);
        faqDetailsRecyclerViewAdapter = new FAQDetailsRecyclerViewAdapter(this, new ArrayList<>());

        rvTabs.setAdapter(faqTabsRecyclerViewAdapter);

        rvQuestions.setAdapter(faqDetailsRecyclerViewAdapter);

        sendFAQRequest();
    }

    private void sendFAQRequest() {
        LoaderPopUp.show(this);
        APIConnectionNetwork.GetFAQ(this);
    }

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

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {
        LoaderPopUp.dismissLoader();
        List<FAQ> faqs = FAQ.parseArray(jsonArray);
        faqTabsRecyclerViewAdapter.setFaqs(faqs);
        if (faqs.size() != 0)
            faqDetailsRecyclerViewAdapter.setFaqs(faqs.get(0).getDetails());

    }

    @Override
    public void onSelectFAQ(FAQ faq) {
        faqDetailsRecyclerViewAdapter.setFaqs(faq.getDetails());
    }
}
