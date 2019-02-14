package com.gnusl.wow.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.PostsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.PostsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Delegates.PostActionsDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class ProfileMomentsActivity extends AppCompatActivity implements ConnectionDelegate, PostActionsDelegate, OnLoadMoreListener {

    public final static String USER_ID = "user_id";

    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_moments);

        if (!getIntent().hasExtra(USER_ID)) {

            finish();
            return;
        }

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // initialize adapter
        initializeAdapter();

        // send request
        sendPostsRequest();
    }

    private void initializeAdapter(){

        RecyclerView recyclerView = findViewById(R.id.posts_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(this,recyclerView, new ArrayList<>(),this,this,false);
        recyclerView.setAdapter(postsRecyclerViewAdapter);
    }

    private void sendPostsRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(this, "", "loading posts..");

        // send request
        APIConnectionNetwork.GetProfilePosts(getUserId(),this);
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, " Connection Failure", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_SHORT).show();

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
        ArrayList<FeaturePost> posts=FeaturePost.parseJSONArray(jsonArray);

        // notify
        postsRecyclerViewAdapter.setFeaturePosts(posts);
        postsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public int getUserId() {

        return getIntent().getIntExtra(USER_ID, 0);
    }

    public static void launch(Activity activity, int userId){

        Intent intent=new Intent(activity,ProfileMomentsActivity.class);
        intent.putExtra(USER_ID,userId);
        activity.startActivity(intent);
    }

    @Override
    public void onEditPost(FeaturePost post) {

       // CreatePostActivity.launchToEdit(this,post);
    }

    @Override
    public void onDeletePost(FeaturePost post) {

    }

    @Override
    public void onLoadMore() {

    }
}
