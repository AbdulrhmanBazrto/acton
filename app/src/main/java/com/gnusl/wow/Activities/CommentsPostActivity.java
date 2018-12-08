package com.gnusl.wow.Activities;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.CommentsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Comment;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Views.FontedEditText;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class CommentsPostActivity extends SlidingActivity implements ConnectionDelegate {

    public static String POST_ID="post_id";

    private RecyclerView comments_recycler_view;
    private NestedScrollView nestedScrollView;
    private CommentsRecyclerViewAdapter commentsRecyclerViewAdapter;
    private ProgressBar progressBar;
    private int postId;
    private AppCompatImageView send_button;
    private FontedEditText message_edit_text;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void init(Bundle savedInstanceState) {

        setPrimaryColors(
                getResources().getColor(R.color.transparent),
                getResources().getColor(R.color.colorPrimaryDarkMain)
        );

        setContent(R.layout.activity_comments_post);
        setHeaderContent(R.layout.post_test);
        enableFullscreen();

        // find views
        findViews();

        // initialize comments adapter
        CommentsRecyclerView();

        if(getIntent().hasExtra(POST_ID))
            postId=getIntent().getIntExtra(POST_ID,0);
        else
            return;

        // send comments request
        sendCommentsRequest();

    }

    private void findViews(){

        comments_recycler_view=(RecyclerView) findViewById(R.id.recycler_view_comments);
        nestedScrollView=(NestedScrollView) findViewById(R.id.nestedScrollView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        send_button=(AppCompatImageView)findViewById(R.id.send_button);
        message_edit_text=(FontedEditText)findViewById(R.id.message_edit_text);
        message_edit_text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

       // send_button.setOnClickListener(v->clickSendComment());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CommentsRecyclerView() {

        // setup comments adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comments_recycler_view.setLayoutManager(layoutManager);

        comments_recycler_view.setNestedScrollingEnabled(true);

        commentsRecyclerViewAdapter = new CommentsRecyclerViewAdapter(this, new ArrayList<>(), getContentScroller());
        comments_recycler_view.setAdapter(commentsRecyclerViewAdapter);
    }

    private void sendCommentsRequest(){

        // show progress
        progressBar.setVisibility(View.VISIBLE);

        comments_recycler_view.setVisibility(View.GONE);

        // send request
        APIConnectionNetwork.GetAllComments(postId,this);
    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, " Connection Failure", LENGTH_SHORT).show();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_SHORT).show();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }
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
        ArrayList<Comment> comments=Comment.parseJSONArray(jsonArray);

        // notify
        commentsRecyclerViewAdapter.setComments(comments);
        commentsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }
    }
}
