package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.FollowersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class RoomsActivity extends AppCompatActivity implements ConnectionDelegate {

    RoomsRecyclerViewAdapter roomsRecyclerViewAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // initialize adapter
        initializeAdapter();

        // send request
        sendRoomsRequest();
    }

    private void initializeAdapter(){

        RecyclerView recyclerView = findViewById(R.id.rooms_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(roomsRecyclerViewAdapter);
    }

    private void sendRoomsRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(this, "", "loading rooms..");

        // send request
       // APIConnectionNetwork.GetUserRooms(this);
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
        ArrayList<Room> rooms=Room.parseJSONArray(jsonArray);

        // notify
        roomsRecyclerViewAdapter.setRooms(rooms);
        roomsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
