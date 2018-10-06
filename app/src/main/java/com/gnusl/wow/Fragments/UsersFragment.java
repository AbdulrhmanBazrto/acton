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
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private View inflatedView;

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

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img2));
        users.add(new User(R.drawable.img3));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img2));
        users.add(new User(R.drawable.img3));


        UsersRecyclerViewAdapter usersRecyclerViewAdapter= new UsersRecyclerViewAdapter(getContext(), users);
        recyclerView.setAdapter(usersRecyclerViewAdapter);

        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}


