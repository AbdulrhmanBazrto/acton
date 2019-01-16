package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.RoomTypesRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SelectRoomTypeDelegate;
import com.gnusl.wow.Models.RoomType;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class CreateRoomActivity extends AppCompatActivity implements ConnectionDelegate, SelectRoomTypeDelegate {

    private TextView capacityTv;
    private TextView adminsTv;
    private TextView membersTv;
    private TextView levelsTv;

    private RoomTypesRecyclerViewAdapter roomTypesRecyclerViewAdapter;
    private ProgressDialog progressDialog;
    private RoomType roomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        capacityTv = findViewById(R.id.capacity);
        adminsTv = findViewById(R.id.admins);
        membersTv = findViewById(R.id.members);
        levelsTv = findViewById(R.id.levels);

        // back press
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // create channel event
        findViewById(R.id.create_room_btn).setOnClickListener(v -> createRoomRequest());

        // init room adapter
        initializeRoomTypeAdapter();

        // get all room types
        getChannelsTypesRequest();

    }

    private void getChannelsTypesRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(this, "", "loading channels types..");

        // send request
        APIConnectionNetwork.GetChannelsTypes(this);
    }

    private void initializeRoomTypeAdapter() {

        RecyclerView recyclerView = findViewById(R.id.room_type_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomTypesRecyclerViewAdapter = new RoomTypesRecyclerViewAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(roomTypesRecyclerViewAdapter);
    }

    private void createRoomRequest() {

        if (roomType == null)
            Toast.makeText(this, "you must choose room type", Toast.LENGTH_SHORT).show();

        else {

            // make progress dialog
            this.progressDialog = ProgressDialog.show(this, "", "create channel..");

            // send request
            APIConnectionNetwork.CreateNewRoom(roomType.getId(), this);
        }
    }

    private void showRoomDetails(RoomType roomType) {

        // capacity
        capacityTv.setText(getResources().getString(R.string.room_capacity_content, String.valueOf(roomType.getCpacity())));

        // admins
        adminsTv.setText(getResources().getString(R.string.room_admins_content));

        // members
        membersTv.setText(getResources().getString(R.string.room_members_content, String.valueOf(roomType.getMember())));

        // levels
        levelsTv.setText(getResources().getString(R.string.room_level_content, String.valueOf(roomType.getLevel())));

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

        if (progressDialog != null)
            progressDialog.dismiss();

        if (jsonObject.has("channel_id")) {
            Toast.makeText(this, "create channel success", LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        if (progressDialog != null)
            progressDialog.dismiss();

        // parsing
        ArrayList<RoomType> roomTypes = RoomType.parseJSONArray(jsonArray);

        // refresh
        roomTypesRecyclerViewAdapter.setRoomTypes(roomTypes);
        roomTypesRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSelectedRoomType(RoomType roomType) {

        this.roomType=roomType;

        // show details
        showRoomDetails(roomType);
    }
}
