package com.gnusl.wow.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.AllBadgesRecyclerViewAdapter;
import com.gnusl.wow.Adapters.ThemesRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Background;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomBackgroundActivity extends AppCompatActivity {

    public final static String CHANNEL_KEY = "channel_key";
    private Room room;

    private RecyclerView rvThemes;
    private ThemesRecyclerViewAdapter themesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_background);

        if (getIntent().hasExtra(CHANNEL_KEY))
            setRoom(getIntent().getParcelableExtra(CHANNEL_KEY));
        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        rvThemes = findViewById(R.id.rv_themes);

        setUpBackgroundRecycler();
    }

    private void setUpBackgroundRecycler() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvThemes.setLayoutManager(gridLayoutManager);
        themesRecyclerViewAdapter = new ThemesRecyclerViewAdapter(this, new ArrayList<>(),getRoom());
        rvThemes.setAdapter(themesRecyclerViewAdapter);

        APIConnectionNetwork.GetRoomBackgrouns(new ConnectionDelegate() {
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
                ArrayList<Background> backgrounds = Background.parseJSONArray(jsonArray);
                themesRecyclerViewAdapter.setUsers(backgrounds);
            }
        });
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
