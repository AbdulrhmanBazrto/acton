package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.CreateRoomActivity;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.SystemMessagesRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.SystemMessage;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SystemMessagesFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener {

    private View inflatedView;
    private SystemMessagesRecyclerViewAdapter systemMessagesRecyclerViewAdapter;


    public SystemMessagesFragment() {
    }

    public static SystemMessagesFragment newInstance() {

        return new SystemMessagesFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_system_messages, container, false);

        // initialize adapter
        RecyclerView recyclerView = inflatedView.findViewById(R.id.system_messages_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        systemMessagesRecyclerViewAdapter = new SystemMessagesRecyclerViewAdapter(getContext(), new ArrayList<>(),recyclerView,this);
        recyclerView.setAdapter(systemMessagesRecyclerViewAdapter);

        sendMessagesRequest();

        return inflatedView;
    }


    private void sendMessagesRequest() {

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetSystemMessages(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null && systemMessagesRecyclerViewAdapter!=null) {

        }
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

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
        ArrayList<SystemMessage> systemMessages=SystemMessage.parseJSONArray(jsonArray);

        // notify
        systemMessagesRecyclerViewAdapter.setMessages(systemMessages);
        systemMessagesRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onLoadMore() {

    }
}


