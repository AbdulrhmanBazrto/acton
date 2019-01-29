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
import com.gnusl.wow.Delegates.SearchByRoomDelegate;
import com.gnusl.wow.Delegates.SearchByUsersDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class SearchRoomFragment extends Fragment implements SearchByRoomDelegate {

    private View inflatedView;
    RoomsRecyclerViewAdapter roomsRecyclerViewAdapter;

    public SearchRoomFragment() {
    }

    public static SearchRoomFragment newInstance() {

        return new SearchRoomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_search_room, container, false);

        RecyclerView recyclerView = inflatedView.findViewById(R.id.rooms_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomsRecyclerViewAdapter= new RoomsRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(roomsRecyclerViewAdapter);

        return inflatedView;
    }

    @Override
    public void onSearchResultDone(ArrayList<Room> rooms) {

        // refresh
        if(roomsRecyclerViewAdapter!=null){
            roomsRecyclerViewAdapter.setRooms(rooms);
            roomsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}


