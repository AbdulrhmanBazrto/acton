package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.gnusl.wow.R;

/**
 * Created by Yehia on 9/23/2018.
 */

public class MomentsFragment extends Fragment {

    private View inflatedView;

    public MomentsFragment() {
    }

    public static MomentsFragment newInstance() {

        return new MomentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_moments, container, false);

        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}
