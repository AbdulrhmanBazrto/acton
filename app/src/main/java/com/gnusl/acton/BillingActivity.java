package com.gnusl.acton;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.List;

public class BillingActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    List<String> skuDetails;
    RecyclerView rvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        rvProducts = findViewById(R.id.products);
        skuDetails = new ArrayList<>();
        skuDetails.add("10_dollars_pkg");

        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK)
                    Toast.makeText(BillingActivity.this, "done", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(BillingActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(BillingActivity.this, "disconnected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.loadProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1();
            }
        });
    }

    public void onClick1() {
        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(skuDetails)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();

            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    if (responseCode == BillingClient.BillingResponse.OK) {
                        Toast.makeText(BillingActivity.this, "done", Toast.LENGTH_SHORT).show();
                        initilizeAdapter(skuDetailsList);
                    } else
                        Toast.makeText(BillingActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(BillingActivity.this, "not ready", Toast.LENGTH_SHORT).show();
        }
    }

    private void initilizeAdapter(List<SkuDetails> skuDetailsList) {
        BillingRecyclerViewAdapter billingRecyclerViewAdapter = new BillingRecyclerViewAdapter(this, skuDetailsList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(0))
                        .build();

                billingClient.launchBillingFlow(BillingActivity.this, billingFlowParams);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProducts.setLayoutManager(linearLayoutManager);
        rvProducts.setAdapter(billingRecyclerViewAdapter);
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }
}
