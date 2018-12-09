package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.FeatureRecyclerViewAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Yehia on 9/30/2018.
 */

public class FeaturedFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private FeatureRecyclerViewAdapter featureRecyclerViewAdapter;
    private ProgressDialog progressDialog;

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

        featureRecyclerViewAdapter= new FeatureRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(featureRecyclerViewAdapter);

        return inflatedView;
    }


    private void sendPostsRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading posts..");

        // send request
        APIConnectionNetwork.GetAllFeaturedPosts(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

            // send request
            sendPostsRequest();
        }
    }


    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<FeaturePost> featurePosts=FeaturePost.parseJSONArray(jsonArray);

        // notify
        featureRecyclerViewAdapter.setFeaturePosts(featurePosts);
        featureRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Subscribe
    public void onRefreshPostsEvent(String message) {

        if(message.equalsIgnoreCase("Refresh_Posts"))
            getActivity().runOnUiThread(()->sendPostsRequest());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}

