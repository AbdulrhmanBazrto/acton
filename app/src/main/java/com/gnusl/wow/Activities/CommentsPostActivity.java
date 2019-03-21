package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.CommentsRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.CommentActionsDelegate;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.Comment;
import com.gnusl.wow.R;
import com.gnusl.wow.SlidingPopUp.PopupActivity;
import com.gnusl.wow.Views.FontedEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;


public class CommentsPostActivity extends PopupActivity implements ConnectionDelegate, CommentActionsDelegate {

    public static String POST_ID = "post_id";

    private RecyclerView comments_recycler_view;
    private NestedScrollView nestedScrollView;
    private CommentsRecyclerViewAdapter commentsRecyclerViewAdapter;
    private ProgressBar progressBar;
    private int postId;
    private AppCompatImageView send_button;
    private FontedEditText message_edit_text;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comments_post);
        super.onCreate(savedInstanceState);

        // find views
        findViews();

        // initialize comments adapter
        CommentsRecyclerView();

        if (getIntent().hasExtra(POST_ID))
            postId = getIntent().getIntExtra(POST_ID, 0);
        else
            return;

        // send comments request
        sendCommentsRequest();
    }

    @Override
    public void didAppear() {

    }

    private void findViews() {

        comments_recycler_view = findViewById(R.id.recycler_view_comments);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        send_button = findViewById(R.id.send_button);
        message_edit_text = findViewById(R.id.message_edit_text);
        message_edit_text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        send_button.setOnClickListener(v -> clickSendComment());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void CommentsRecyclerView() {

        // setup comments adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comments_recycler_view.setLayoutManager(layoutManager);

        comments_recycler_view.setNestedScrollingEnabled(true);

        commentsRecyclerViewAdapter = new CommentsRecyclerViewAdapter(this, new ArrayList<>(), this);
        comments_recycler_view.setAdapter(commentsRecyclerViewAdapter);
    }

    private void sendCommentsRequest() {

        // show progress
        progressBar.setVisibility(View.VISIBLE);

        comments_recycler_view.setVisibility(View.GONE);

        // send request
        APIConnectionNetwork.GetAllComments(postId, this);
    }

    private void clickSendComment() {

        if (message_edit_text.getText().toString().isEmpty())
            Toast.makeText(this, "you must have message", Toast.LENGTH_SHORT).show();

        else {

            // add comment
            APIConnectionNetwork.AddNewComment(message_edit_text.getText().toString(), postId, this);

            // clear message
            message_edit_text.setText("");
        }

    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, " Connection Failure", LENGTH_SHORT).show();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "Error Connection try again", LENGTH_SHORT).show();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        if (progressDialog != null)
            progressDialog.dismiss();

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }

        // refresh comments
        APIConnectionNetwork.GetAllComments(postId, this);
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<Comment> comments = Comment.parseJSONArray(jsonArray);

        // notify
        commentsRecyclerViewAdapter.setComments(comments);
        commentsRecyclerViewAdapter.notifyDataSetChanged();

        // dismiss
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            comments_recycler_view.setVisibility(View.VISIBLE);
        }

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onEditComment(Comment comment) {

        // show popup
        new MaterialDialog.Builder(this)
                .title("Edit your Comment")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter your comment", comment.getComment(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something

                        progressDialog = ProgressDialog.show(CommentsPostActivity.this, "", getString(R.string.updating_comment));

                        // send request
                        APIConnectionNetwork.UpdateComment(comment.getId(), input.toString(), CommentsPostActivity.this);
                    }
                }).show();
    }

    @Override
    public void onDeleteComment(Comment comment) {

        // send delete request
        APIConnectionNetwork.DeleteComment(comment.getId(), this);

        // show progress
        progressBar.setVisibility(View.VISIBLE);

        comments_recycler_view.setVisibility(View.GONE);

    }
}
