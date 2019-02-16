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
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SearchDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class SearchUsersFragment extends Fragment implements SearchDelegate,ConnectionDelegate {

    private View inflatedView;
    UsersRecyclerViewAdapter usersRecyclerViewAdapter;

    public SearchUsersFragment() {
    }

    public static SearchUsersFragment newInstance() {

        return new SearchUsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_users, container, false);

        RecyclerView recyclerView = inflatedView.findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(usersRecyclerViewAdapter);

        // send request
        APIConnectionNetwork.SearchForUsers("", this);

        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

    @Override
    public void onSearchByTag(String searchContent) {

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // search users
        APIConnectionNetwork.SearchForUsers(searchContent, this);
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), "Connection Failure", LENGTH_SHORT).show();

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
        ArrayList<User> users = User.parseJSONArray(jsonArray);

        // refresh
        if (usersRecyclerViewAdapter != null) {
            usersRecyclerViewAdapter.setUsers(users);
            usersRecyclerViewAdapter.notifyDataSetChanged();
        }

        // dismiss
        LoaderPopUp.dismissLoader();
    }

}


