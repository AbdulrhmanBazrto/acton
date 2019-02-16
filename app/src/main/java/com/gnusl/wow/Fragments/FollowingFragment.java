package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.CreatePostActivity;
import com.gnusl.wow.Adapters.PostsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Delegates.PostActionsDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Yehia on 9/30/2018.
 */

public class FollowingFragment extends Fragment implements ConnectionDelegate, PostActionsDelegate, OnLoadMoreListener {

    private static final int PAGE_SIZE_ITEMS = 5;
    private boolean isRefreshing;
    private View inflatedView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;

    public FollowingFragment() {
    }

    public static FollowingFragment newInstance() {

        return new FollowingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_following, container, false);


        RecyclerView recyclerView = inflatedView.findViewById(R.id.following_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(getContext(),recyclerView, new ArrayList<>(), this,this,true);
        recyclerView.setAdapter(postsRecyclerViewAdapter);

        // send request
        APIConnectionNetwork.GetAllFollowingPosts(PAGE_SIZE_ITEMS, postsRecyclerViewAdapter.getFeaturePosts().size(), this);

        return inflatedView;
    }


    private void sendPostsRequest() {

        if (postsRecyclerViewAdapter == null)
            return;

        isRefreshing = true;

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetAllFollowingPosts(PAGE_SIZE_ITEMS, postsRecyclerViewAdapter.getFeaturePosts().size(), this);
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

        LoaderPopUp.dismissLoader();

        // disable loading
        if (postsRecyclerViewAdapter != null) {
            postsRecyclerViewAdapter.setLoading(false);
            postsRecyclerViewAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

        // disable loading
        if (postsRecyclerViewAdapter != null) {
            postsRecyclerViewAdapter.setLoading(false);
            postsRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        // dismiss
        LoaderPopUp.dismissLoader();

        // refresh posts
        sendPostsRequest();

    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        if (postsRecyclerViewAdapter != null) {
            // parsing
            ArrayList<FeaturePost> featurePosts = FeaturePost.parseJSONArray(jsonArray);

            // disable loading
            postsRecyclerViewAdapter.setLoading(false);

            // notify
            if (postsRecyclerViewAdapter.getFeaturePosts().isEmpty()) {

                postsRecyclerViewAdapter.setFeaturePosts(featurePosts);
                postsRecyclerViewAdapter.notifyDataSetChanged();

            } else {
                int position = postsRecyclerViewAdapter.getFeaturePosts().size();
                postsRecyclerViewAdapter.getFeaturePosts().addAll(featurePosts);
                postsRecyclerViewAdapter.notifyDataSetChanged();
            }

            // check limit
            if(featurePosts.isEmpty())
                postsRecyclerViewAdapter.setRechToLimit(true);

            isRefreshing = false;

        }

        // dismiss
        LoaderPopUp.dismissLoader();
    }

    @Subscribe
    public void onRefreshPostsEvent(String message) {

        if (message.equalsIgnoreCase("Refresh_Posts"))
            getActivity().runOnUiThread(() -> sendPostsRequest());
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

    @Override
    public void onEditPost(FeaturePost post) {

        Intent intent=new Intent(getActivity(),CreatePostActivity.class);
        intent.putExtra(CreatePostActivity.UPDATE_POST_KEY,post);
        startActivity(intent);
    }

    @Override
    public void onDeletePost(FeaturePost post) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getString(R.string.are_you_sure_delete_post));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                (dialog, which) -> {

                    // make progress dialog
                    LoaderPopUp.show(getActivity());

                    // send request
                    APIConnectionNetwork.DeletePost(post.getId(), this);

                    // dismiss
                    dialog.dismiss();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void onLoadMore() {

        if (!isRefreshing && !postsRecyclerViewAdapter.isRechToLimit())
            // send request
            sendPostsRequest();
    }
}
