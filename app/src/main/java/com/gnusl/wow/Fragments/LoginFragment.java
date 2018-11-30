package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.R;

public class LoginFragment extends Fragment {

    private View inflatedView;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_login, container, false);


        return inflatedView;
    }
}

