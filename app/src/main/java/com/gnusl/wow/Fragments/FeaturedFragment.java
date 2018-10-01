package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Adapters.FeatureRecyclerViewAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/30/2018.
 */

public class FeaturedFragment extends Fragment {

    private View inflatedView;

    public FeaturedFragment() {
    }

    public static FeaturedFragment newInstance() {

        return new FeaturedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_featured, container, false);


        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.featured_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<FeaturePost> featurePosts= new ArrayList<>();
        featurePosts.add(new FeaturePost());
        featurePosts.add(new FeaturePost());
        featurePosts.add(new FeaturePost());

        FeatureRecyclerViewAdapter featureRecyclerViewAdapter= new FeatureRecyclerViewAdapter(getContext(), featurePosts);
        recyclerView.setAdapter(featureRecyclerViewAdapter);

        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}

