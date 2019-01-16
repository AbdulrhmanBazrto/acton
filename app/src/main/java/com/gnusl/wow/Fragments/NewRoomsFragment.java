package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Yehia on 9/27/2018.
 */

public class NewRoomsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private RoomsRecyclerViewAdapter roomsRecyclerViewAdapter;
    private ProgressDialog progressDialog;

    public NewRoomsFragment() {
    }

    public static NewRoomsFragment newInstance() {

        return new NewRoomsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_new_rooms, container, false);

        // initialize adapter
        RecyclerView recyclerView = inflatedView.findViewById(R.id.new_rooms_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(roomsRecyclerViewAdapter);

        // send request
        sendChannelsRequest();

        return inflatedView;
    }

    private void sendChannelsRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading channels..");

        // send request
        APIConnectionNetwork.GetAllChannels(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null && roomsRecyclerViewAdapter!=null && roomsRecyclerViewAdapter.getRooms().isEmpty()) {

            // send request
            sendChannelsRequest();
        }
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", Toast.LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

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

