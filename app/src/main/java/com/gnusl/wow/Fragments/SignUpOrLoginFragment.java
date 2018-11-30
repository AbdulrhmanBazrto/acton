package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.RegisterActivity;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.R;

public class SignUpOrLoginFragment extends Fragment {

    private View inflatedView;

    public SignUpOrLoginFragment() {
    }

    public static SignUpOrLoginFragment newInstance() {

        return new SignUpOrLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_signup_or_login, container, false);

        inflatedView.findViewById(R.id.sign_up_btn).setOnClickListener(v->{

            ((RegisterActivity)getActivity()).makeFragment(FragmentTags.SignUpFragment);
        });

        inflatedView.findViewById(R.id.Login_button).setOnClickListener(v->{

            ((RegisterActivity)getActivity()).makeFragment(FragmentTags.LoginFragment);
        });

        return inflatedView;
    }
}
