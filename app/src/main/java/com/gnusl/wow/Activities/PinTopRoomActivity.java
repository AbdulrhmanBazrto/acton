package com.gnusl.wow.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Fragments.StoreRoomLockFragment;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PinTopRoomActivity extends AppCompatActivity {

    TextView tbMonthValue, tvSubPrice;
    Button btnSub;
    int currentId;
    private AlertDialog alert = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_top_room);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        tbMonthValue = findViewById(R.id.month_value);
        tvSubPrice = findViewById(R.id.tv_sub_price);
        btnSub = findViewById(R.id.btn_sub);

        btnSub.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PinTopRoomActivity.this);
            builder.setMessage("تأكيد العملية؟")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            subscripeToPinTop();
                            if (alert != null)
                                alert.hide();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (alert != null)
                                alert.hide();
                        }
                    });
            alert = builder.create();
            alert.show();

        });
        getDuration();

    }

    private void subscripeToPinTop() {
        LoaderPopUp.show(this);
        APIConnectionNetwork.ChangeRoomPriority(currentId, new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionError(ANError anError) {
                LoaderPopUp.dismissLoader();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(anError.getErrorBody());
                    if (jsonObject.has("payment_status")) {
                        if (jsonObject.optString("payment_status").equalsIgnoreCase("error")) {
                            startActivity(new Intent(PinTopRoomActivity.this, RechargeActivity.class));
                        }
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onConnectionSuccess(String response) {
                LoaderPopUp.dismissLoader();
                finish();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                LoaderPopUp.dismissLoader();
                finish();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
                finish();
            }
        });
    }

    private void getDuration() {
        LoaderPopUp.show(this);

        APIConnectionNetwork.GetDurationOfPintTop(new ConnectionDelegate() {
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
                LoaderPopUp.dismissLoader();
                tbMonthValue.setText(String.format(getString(R.string.days_), jsonObject.optString("duration")));
                tvSubPrice.setText(jsonObject.optString("price"));
                currentId = jsonObject.optInt("channel_id");
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }

}
