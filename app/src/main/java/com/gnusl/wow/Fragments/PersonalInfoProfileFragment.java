package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Models.RefreshGiftsDelegate;
import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;

public class PersonalInfoProfileFragment extends Fragment {

    private View inflatedView;
    private TextView countryCodeTv;

    public PersonalInfoProfileFragment() {
    }

    public static PersonalInfoProfileFragment newInstance() {

        return new PersonalInfoProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_personal_profile_info, container, false);

        countryCodeTv=inflatedView.findViewById(R.id.city);

        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {


        }
    }

    public TextView getCountryCodeTv() {
        return countryCodeTv;
    }

    public void setCountryCodeTv(TextView countryCodeTv) {
        this.countryCodeTv = countryCodeTv;
    }
}
