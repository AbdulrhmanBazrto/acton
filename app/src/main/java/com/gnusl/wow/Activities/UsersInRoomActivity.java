package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.UsersInRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class UsersInRoomActivity extends AppCompatActivity implements ConnectionDelegate {

    public final static String CHANNEL_KEY="channel_key";
    private Room room;

    private UsersInRoomRecyclerViewAdapter usersInRoomRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_room);

        if(getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        // init adapter
        initializeAdapter();

        // send request
        sendUsersInRoomDataRequest();

    }

    private void initializeAdapter(){

        RecyclerView recyclerView = findViewById(R.id.members_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersInRoomRecyclerViewAdapter = new UsersInRoomRecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(usersInRoomRecyclerViewAdapter);
    }

    private void sendUsersInRoomDataRequest(){

        // make progress dialog
        LoaderPopUp.show(this);

        // get users
        APIConnectionNetwork.GetUserAttendance(getRoom().getId(), this);
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, "Connection Failure", LENGTH_SHORT).show();

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

        if (jsonObject.has("users")) { // refresh scores in room
            try {
                usersInRoomRecyclerViewAdapter.setUsers(User.parseJSONArray(jsonObject.getJSONArray("users")));
                usersInRoomRecyclerViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // dismiss
        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
