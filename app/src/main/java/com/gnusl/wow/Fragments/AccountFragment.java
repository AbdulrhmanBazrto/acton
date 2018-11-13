package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.R;

public class AccountFragment extends Fragment {

    private View inflatedView;

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {

        return new AccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_account, container, false);


        return inflatedView;
    }
}