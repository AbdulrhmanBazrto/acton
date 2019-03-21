package com.gnusl.wow.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.gnusl.wow.Adapters.MediaGridViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.gnusl.wow.Views.FontedEditText;
import com.gnusl.wow.Views.FontedTextView;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.CALENDAR_PERMISSION;
import static com.gnusl.wow.Utils.PermissionsUtils.CAMERA_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkCameraPermissions;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;

public class CreatePostActivity extends AppCompatActivity implements ConnectionDelegate {

    public static String UPDATE_POST_KEY="update_post_key";
    private CircularImageView profile_image;
    private AutoFitFontedTextView name;
    private FontedEditText text;
    private FeaturePost featurePost;
    private boolean isEditMode=false;
    BroadcastReceiver videoBroadcastReceiver;
    BroadcastReceiver imageBroadcastReceiver;
    GridView gridView;
    MediaGridViewAdapter adapter;
    private boolean mustBeRefreshPosts = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.create_post));
        setSupportActionBar(toolbar);

        // find views
        finViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        // Media Picker
        InitializeMediaPicker();

        // check mode
        CheckEditMode();
    }

    private void CheckEditMode(){

        // handle edit mode
        if(getIntent().hasExtra(UPDATE_POST_KEY)){
            isEditMode=true;

            featurePost=getIntent().getParcelableExtra(UPDATE_POST_KEY);

            // text
            text.setText(featurePost.getDescription());
            text.setSelection(featurePost.getDescription().length());

            // user image
            if (featurePost.getUser() != null && featurePost.getUser().getImage_url()!=null && !featurePost.getUser().getImage_url().isEmpty())
                Glide.with(this)
                        .load(featurePost.getUser().getImage_url())
                        .into(profile_image);
        }
    }

    private void setAdapter(List<String> filePathList) {
        adapter.addAll(filePathList);
        adapter.notifyDataSetChanged();
    }

    public void openGallery() {

        if (checkReadGalleryPermissions(this)) {

            HomeScreenMediaChooser.startMediaChooser(this, true);

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSIONS_REQUEST);

        }

    }

    private void finViews() {

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        text = findViewById(R.id.text);
        gridView = findViewById(R.id.gridView);

        profile_image.setOnClickListener(v -> {

            openGallery();
        });

    }

    private void shareYourPost() {

        if (text.getText().toString().isEmpty())
            Toast.makeText(this, getString(R.string.create_post_hint), Toast.LENGTH_SHORT).show();

        else {

            ArrayList<File> files=adapter.getImagesAsFiles();

            if(!files.isEmpty()){

                // make progress dialog
                LoaderPopUp.show(this);

                // upload image
                APIConnectionNetwork.UploadImage(adapter.getImagesAsFiles().get(0),this);

            }else
                sendUploadPostRequest(null);

        }

    }

    private void sendUploadPostRequest(String imageName){

        if(!isEditMode) {

            // make progress dialog
            LoaderPopUp.show(this);

            // send request
            APIConnectionNetwork.CreateNewPost(text.getText().toString(), imageName, this);

        }else {

            // make progress dialog
            LoaderPopUp.show(this);

            // send request
            APIConnectionNetwork.UpdatePost(featurePost.getId(),text.getText().toString(), this);

        }
    }

    private void InitializeMediaPicker() {

        adapter = new MediaGridViewAdapter(this, 0, new ArrayList<>());
        gridView.setAdapter(adapter);

        videoBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(CreatePostActivity.this, "yippiee Video ", Toast.LENGTH_SHORT).show();
                Toast.makeText(CreatePostActivity.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                setAdapter(intent.getStringArrayListExtra("list"));
            }
        };


        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(CreatePostActivity.this, "yippiee Image ", Toast.LENGTH_SHORT).show();
                Toast.makeText(CreatePostActivity.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                setAdapter(intent.getStringArrayListExtra("list"));
            }
        };


        IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        MediaChooser.showCameraVideoView(false);

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

        LoaderPopUp.dismissLoader();


    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(this, "error Connection try again", LENGTH_LONG).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        LoaderPopUp.dismissLoader();

        if (jsonObject.has("post_id")) {
            Toast.makeText(this, getString(R.string.success_share), Toast.LENGTH_SHORT).show();
            mustBeRefreshPosts = true;
            finish();

        }else if(jsonObject.has("success")) { // update post

            Toast.makeText(this, getString(R.string.success_updating), Toast.LENGTH_SHORT).show();
            mustBeRefreshPosts = true;
            finish();

        } else if(jsonObject.has("image")){

            // upload post
            try {
                sendUploadPostRequest(jsonObject.getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_PERMISSIONS_REQUEST) {


            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                if (grantResults.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        //Coming from profile
                        HomeScreenMediaChooser.startMediaChooser(this, true);

                    } else {
                        Toast.makeText(this, R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                    }

                }

            }

        }

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(imageBroadcastReceiver);
        super.onDestroy();

        // send
        if (mustBeRefreshPosts)
            EventBus.getDefault().post("Refresh_Posts");
    }

    public static void launch(Activity activity){

        Intent intent = new Intent(activity, CreatePostActivity.class);
        activity.startActivity(intent);
    }

    public static void launchToEdit(Activity activity,FeaturePost post){

        Intent intent = new Intent(activity, CreatePostActivity.class);
        intent.putExtra(CreatePostActivity.UPDATE_POST_KEY, post);
        activity.startActivity(intent);
    }
}
