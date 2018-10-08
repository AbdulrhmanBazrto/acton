package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Adapters.MessageRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/23/2018.
 */

public class MessagesFragment extends Fragment {

    private View inflatedView;

    public MessagesFragment() {
    }

    public static MessagesFragment newInstance() {

        return new MessagesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_messages, container, false);


        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v->((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START));


        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.message_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Message> messages= new ArrayList<>();
        messages.add(new Message(R.drawable.friends,"New Friends"));
        messages.add(new Message(R.drawable.system,"System"));
        messages.add(new Message(R.drawable.acivity,"Activity"));


        MessageRecyclerViewAdapter messageRecyclerViewAdapter= new MessageRecyclerViewAdapter(getContext(), messages);
        recyclerView.setAdapter(messageRecyclerViewAdapter);


        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}

