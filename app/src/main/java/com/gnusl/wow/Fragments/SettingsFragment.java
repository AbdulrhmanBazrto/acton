package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.SettingsActivity;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private View inflatedView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);

        inflatedView.findViewById(R.id.accout_button).setOnClickListener(v->((SettingsActivity)getActivity()).makeFragment(FragmentTags.AccountFragment));

        return inflatedView;
    }
}


