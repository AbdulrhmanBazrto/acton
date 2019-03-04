package com.gnusl.wow.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.RechargeRecyclerViewAdapter;
import com.gnusl.wow.BuildConfig;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.ReChargeDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    List<String> skuDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        getPurchasesPackages();

    }

    private void getPurchasesPackages() {

        APIConnectionNetwork.GetPurchasesPackages(new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

            }

            @Override
            public void onConnectionError(ANError anError) {

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                skuDetails = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    skuDetails.add(jsonArray.optJSONObject(i).optString("sku"));
                }

                billingClient = BillingClient.newBuilder(RechargeActivity.this)
                        .setListener(RechargeActivity.this)
                        .build();

                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(int responseCode) {
                        if (responseCode == BillingClient.BillingResponse.OK) {
                            if (billingClient.isReady()) {
                                SkuDetailsParams params = SkuDetailsParams.newBuilder()
                                        .setSkusList(skuDetails)
                                        .setType(BillingClient.SkuType.INAPP)
                                        .build();

                                billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                                    @Override
                                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                                        if (responseCode == BillingClient.BillingResponse.OK) {
                                            initilizeAdapter(skuDetailsList);
                                        } else
                                            Toast.makeText(RechargeActivity.this, "error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(RechargeActivity.this, "not ready", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(RechargeActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        Toast.makeText(RechargeActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initilizeAdapter(List<SkuDetails> skuDetailsList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        RechargeRecyclerViewAdapter rechargeRecyclerViewAdapter = new RechargeRecyclerViewAdapter(this, skuDetailsList, skuDetails -> {
            if (BuildConfig.DEBUG) {
                storePayment(skuDetails.getSku());
            } else {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .setAccountId(String.valueOf(SharedPreferencesUtils.getUser().getId()))
                        .build();

                billingClient.launchBillingFlow(RechargeActivity.this, billingFlowParams);
            }
        });
        recyclerView.setAdapter(rechargeRecyclerViewAdapter);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        // todo call store payment api,
        if (responseCode == 200) {
            if (purchases != null && purchases.size() > 0)
                storePayment(purchases.get(0).getSku());
        }
    }

    public void storePayment(String sku) {
        APIConnectionNetwork.StorePayment(sku, new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
                Toast.makeText(RechargeActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionError(ANError anError) {
                Toast.makeText(RechargeActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(String response) {
                Toast.makeText(RechargeActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                Toast.makeText(RechargeActivity.this, "done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                Toast.makeText(RechargeActivity.this, "done", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
