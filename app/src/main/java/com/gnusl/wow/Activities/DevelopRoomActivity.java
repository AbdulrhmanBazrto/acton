package com.gnusl.wow.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.RoomTypesRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SelectRoomTypeDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.RoomType;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class DevelopRoomActivity extends AppCompatActivity implements ConnectionDelegate, SelectRoomTypeDelegate {

    private TextView capacityTv;
    private TextView adminsTv;
    private TextView membersTv;
    private TextView levelsTv;

    public final static String CHANNEL_KEY = "channel_key";
    private Room room;

    private RoomTypesRecyclerViewAdapter roomTypesRecyclerViewAdapter;
    private RoomType roomType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_room);

        if (getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));

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
        LoaderPopUp.show(this);

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
            Toast.makeText(this, getString(R.string.room_type_hint), Toast.LENGTH_SHORT).show();

        else {

            // make progress dialog
            LoaderPopUp.show(this);

            // send request
            APIConnectionNetwork.ChangeRoomType(roomType.getId(), room.getId(), this);
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

        // parse room
        Room room = Room.newInstance(jsonObject);

        LoaderPopUp.dismissLoader();

        Toast.makeText(this, getString(R.string.update_channel_success), LENGTH_SHORT).show();
        finish();

        // go to new room
        RoomChatActivity.launch(this, room);

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        LoaderPopUp.dismissLoader();

        // parsing
        ArrayList<RoomType> roomTypes = RoomType.parseJSONArray(jsonArray);

        // refresh
        roomTypesRecyclerViewAdapter.setRoomTypes(roomTypes);
        roomTypesRecyclerViewAdapter.notifyDataSetChanged();

        // set standard selection
        if (!roomTypes.isEmpty())
            onSelectedRoomType(roomTypes.get(0));
    }

    @Override
    public void onSelectedRoomType(RoomType roomType) {

        this.roomType = roomType;

        // show details
        showRoomDetails(roomType);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
