package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yehia on 9/23/2018.
 */

public class RoomFragment extends Fragment {

    private View inflatedView;

    public RoomFragment() {
    }

    public static RoomFragment newInstance() {

        return new RoomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room, container, false);


        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}

