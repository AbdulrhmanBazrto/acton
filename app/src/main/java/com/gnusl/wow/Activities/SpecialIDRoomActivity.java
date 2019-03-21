package com.gnusl.wow.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.AllBadgesRecyclerViewAdapter;
import com.gnusl.wow.Adapters.SpecialIDsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SpecialIDClickDelegate;
import com.gnusl.wow.Fragments.StoreRoomLockFragment;
import com.gnusl.wow.Models.RoomLockType;
import com.gnusl.wow.Models.SpecialID;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class SpecialIDRoomActivity extends AppCompatActivity implements SpecialIDClickDelegate {

    TextView tvCurrentId;
    RecyclerView rvSpecialIds;
    int currentId;

    SpecialIDsRecyclerViewAdapter specialIDsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_id);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());


        tvCurrentId = findViewById(R.id.tv_current_id);
        rvSpecialIds = findViewById(R.id.rv_special_ids);

        getSpecialIds();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvSpecialIds.setLayoutManager(gridLayoutManager);
        specialIDsRecyclerViewAdapter = new SpecialIDsRecyclerViewAdapter(this, new ArrayList<>(), this);
        rvSpecialIds.setAdapter(specialIDsRecyclerViewAdapter);

    }

    private void getSpecialIds() {

        LoaderPopUp.show(this);

        APIConnectionNetwork.GetSpecialIds(new ConnectionDelegate() {
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
                currentId = Integer.parseInt(response);
                tvCurrentId.setText("ID: " + Integer.parseInt(response));
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
                ArrayList<SpecialID> specialIDS = SpecialID.parseJSONArray(jsonArray);
                specialIDsRecyclerViewAdapter.setBadges(specialIDS);

            }
        });
    }

    private AlertDialog alert = null;

    @Override
    public void onClick(SpecialID specialID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("تأكيد العملية؟")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        APIConnectionNetwork.UpdateRoomID(specialID.getTxtId(), currentId, null);
                        if (alert != null)
                            alert.hide();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alert != null)
                            alert.hide();
                    }
                });
        alert = builder.create();
        alert.show();

    }
}
