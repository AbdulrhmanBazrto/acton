package com.gnusl.wow.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.MessagesConversationActivity;
import com.gnusl.wow.Adapters.MessageRecyclerViewAdapter;
import com.gnusl.wow.Delegates.MessageSectionDelegate;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.MessageSection;
import com.gnusl.wow.R;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by Yehia on 9/23/2018.
 */

@SuppressLint("ValidFragment")
public class MessagesFragment extends Fragment {

    private View inflatedView;
    private MessageSectionDelegate messageSectionDelegate;

    @SuppressLint("ValidFragment")
    public MessagesFragment(MessageSectionDelegate messageSectionDelegate) {
        this.messageSectionDelegate=messageSectionDelegate;
    }

    public MessagesFragment(){

    }

    public static MessagesFragment newInstance(MessageSectionDelegate messageSectionDelegate) {

        return new MessagesFragment(messageSectionDelegate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_messages, container, false);

        RecyclerView recyclerView = inflatedView.findViewById(R.id.message_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<MessageSection> messageSections = new ArrayList<>();
        messageSections.add(new MessageSection(R.drawable.friends, getString(R.string.my_friends)));
        messageSections.add(new MessageSection(R.drawable.system, getString(R.string.system)));
       // messageSections.add(new MessageSection(R.drawable.acivity, "Activity"));


        MessageRecyclerViewAdapter messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(getContext(), messageSections,messageSectionDelegate);
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

