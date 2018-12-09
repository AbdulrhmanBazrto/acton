package com.gnusl.wow.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.gnusl.wow.Views.FontedEditText;
import com.gnusl.wow.Views.FontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class CreatePostActivity extends AppCompatActivity implements ConnectionDelegate {

    private CircularImageView profile_image;
    private AutoFitFontedTextView name;
    private FontedEditText text;
    private ProgressDialog progressDialog;

    BroadcastReceiver videoBroadcastReceiver;
    BroadcastReceiver imageBroadcastReceiver;
    GridView gridView;
    //MediaGridViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.create_post));
        setSupportActionBar(toolbar);

        finViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

    }

    public void openGallery() {

      //  HomeScreenMediaChooser.startMediaChooser(this, false);

    }

    private void finViews() {

        profile_image = (CircularImageView) findViewById(R.id.profile_image);
        name = (AutoFitFontedTextView) findViewById(R.id.name);
        text = (FontedEditText) findViewById(R.id.text);
        gridView = (GridView) findViewById(R.id.gridView);

    }

    private void shareYourPost() {

        if (text.getText().toString().isEmpty())
            Toast.makeText(this, "you must have something to share it", Toast.LENGTH_SHORT).show();

        else {

            // make progress dialog
            this.progressDialog = ProgressDialog.show(this, "", "uploading your post..");

            // send request
            APIConnectionNetwork.CreateNewPost(text.getText().toString(),this);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            shareYourPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(this, "failure with share your post..", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();


    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "error Connection try again", LENGTH_LONG).show();

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

        if(jsonObject.has("post_id")) {
            Toast.makeText(this, "success share..", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
