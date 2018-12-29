package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Delegates.SearchByUsersDelegate;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class UsersFragment extends Fragment implements SearchByUsersDelegate {

    private View inflatedView;
    UsersRecyclerViewAdapter usersRecyclerViewAdapter;

    public UsersFragment() {
    }

    public static UsersFragment newInstance() {

        return new UsersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_users, container, false);

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersRecyclerViewAdapter= new UsersRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(usersRecyclerViewAdapter);

        return inflatedView;
    }

    @Override
    public void onSearchResultDone(ArrayList<User> users) {

        // refresh
        if(usersRecyclerViewAdapter!=null){
            usersRecyclerViewAdapter.setUsers(users);
            usersRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }
}


