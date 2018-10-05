package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yehia on 9/27/2018.
 */

public class NewRoomsFragment extends Fragment {

    private View inflatedView;

    public NewRoomsFragment() {
    }

    public static NewRoomsFragment newInstance() {

        return new NewRoomsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_new_rooms, container, false);

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.new_rooms_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());

        RoomsRecyclerViewAdapter roomsRecyclerViewAdapter = new RoomsRecyclerViewAdapter(getContext(), rooms);
        recyclerView.setAdapter(roomsRecyclerViewAdapter);

        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}
