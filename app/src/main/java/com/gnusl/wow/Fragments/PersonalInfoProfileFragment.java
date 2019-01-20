package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;

public class PersonalInfoProfileFragment extends Fragment {

    private View inflatedView;

    public PersonalInfoProfileFragment() {
    }

    public static PersonalInfoProfileFragment newInstance() {

        return new PersonalInfoProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_personal_profile_info, container, false);


        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {


        }
    }
}
