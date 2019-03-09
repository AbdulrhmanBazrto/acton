package com.gnusl.wow.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.MyRoomsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyRoomsActivity extends AppCompatActivity {

    TextView tvJoinedRooms, tvMyRooms, tvCreateRoom;
    RecyclerView rvJoinedRooms, rvMyRooms;

    MyRoomsRecyclerViewAdapter myRoomsRecyclerViewAdapter;
    MyRoomsRecyclerViewAdapter joinedRoomsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rooms);

        rvJoinedRooms = findViewById(R.id.rv_joined_rooms);
        rvMyRooms = findViewById(R.id.rv_my_rooms);


        tvJoinedRooms = findViewById(R.id.tv_joined_rooms);
        tvMyRooms = findViewById(R.id.tv_my_rooms);
        tvCreateRoom = findViewById(R.id.tv_create_room);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        rvMyRooms.setLayoutManager(linearLayoutManager);
        rvJoinedRooms.setLayoutManager(linearLayoutManager1);

        myRoomsRecyclerViewAdapter = new MyRoomsRecyclerViewAdapter(this, new ArrayList<>());
        joinedRoomsRecyclerViewAdapter = new MyRoomsRecyclerViewAdapter(this, new ArrayList<>());

        rvJoinedRooms.setAdapter(joinedRoomsRecyclerViewAdapter);
        rvMyRooms.setAdapter(myRoomsRecyclerViewAdapter);

        tvCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyRoomsActivity.this, CreateRoomActivity.class));
            }
        });

        APIConnectionNetwork.GetUserChannelInfo(new ConnectionDelegate() {
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
                ArrayList<Room> follows = Room.parseJSONArray(jsonObject.optJSONArray("user_follow_channels"));
                ArrayList<Room> my = Room.parseJSONArray(jsonObject.optJSONArray("user_channels"));

                if (my.size() == 0) {
                    tvMyRooms.setVisibility(View.GONE);
                    rvMyRooms.setVisibility(View.GONE);
                    tvCreateRoom.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rvJoinedRooms.getLayoutParams();
                    params.setMargins(0, 50, 0, 0); //substitute parameters for left, top, right, bottom
                    rvJoinedRooms.setLayoutParams(params);

                } else {
                    tvMyRooms.setVisibility(View.VISIBLE);
                    rvMyRooms.setVisibility(View.VISIBLE);
                    tvCreateRoom.setVisibility(View.GONE);

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) rvJoinedRooms.getLayoutParams();
                    params.setMargins(0, 5, 0, 0); //substitute parameters for left, top, right, bottom
                    rvJoinedRooms.setLayoutParams(params);

                    myRoomsRecyclerViewAdapter.setRooms(my);

                }

                if (follows.size() == 0) {
                    tvJoinedRooms.setVisibility(View.GONE);
                    rvJoinedRooms.setVisibility(View.GONE);
                } else {
                    tvJoinedRooms.setVisibility(View.VISIBLE);
                    rvJoinedRooms.setVisibility(View.VISIBLE);
                }
                joinedRoomsRecyclerViewAdapter.setRooms(follows);
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }
}
