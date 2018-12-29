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
import com.gnusl.wow.Adapters.UsersMessegesAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.UserMessage;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class UsersMessagesFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener {

    private View inflatedView;
    private ProgressDialog progressDialog;
    private UsersMessegesAdapter usersMessegesAdapter;

    public UsersMessagesFragment() {
    }

    public static UsersMessagesFragment newInstance() {

        return new UsersMessagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_users_messages, container, false);

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.users_messages_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        usersMessegesAdapter=new UsersMessegesAdapter(getContext(),new ArrayList<>(),recyclerView,this);
        recyclerView.setAdapter(usersMessegesAdapter);

        sendUsersMessagesRequest();

        return inflatedView;
    }

    private void sendUsersMessagesRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading users messages..");

        // send request
        APIConnectionNetwork.GetUsersMessages(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

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
        ArrayList<UserMessage> userMessages=UserMessage.parseJSONArray(jsonArray);

        // notify
        usersMessegesAdapter.setUserMessages(userMessages);
        usersMessegesAdapter.notifyDataSetChanged();

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onLoadMore() {

    }
}


