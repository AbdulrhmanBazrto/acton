package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.R;

public class BlockListFragment extends Fragment {

    private View inflatedView;

    public BlockListFragment() {
    }

    public static BlockListFragment newInstance() {

        return new BlockListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_block_list, container, false);


        return inflatedView;
    }
}