package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.SettingsActivity;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.R;

public class RoomSettingsFragment extends Fragment {

    private View inflatedView;

    public RoomSettingsFragment() {
    }

    public static RoomSettingsFragment newInstance() {

        return new RoomSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room_settings, container, false);


        return inflatedView;
    }
}
