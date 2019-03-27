package com.gnusl.wow.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Adapters.MediaGridViewAdapter;
import com.gnusl.wow.Application.WowApplication;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.FeaturePost;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.gnusl.wow.Views.FontedEditText;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;
import static com.learnncode.mediachooser.Utilities.MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

public class CreatePostActivity extends AppCompatActivity implements ConnectionDelegate {

    public static String UPDATE_POST_KEY = "update_post_key";
    private CircularImageView profile_image;
    private AutoFitFontedTextView name;
    private FontedEditText text;
    private FeaturePost featurePost;
    private boolean isEditMode = false;
    BroadcastReceiver videoBroadcastReceiver;
    BroadcastReceiver imageBroadcastReceiver;
    GridView gridView;
    MediaGridViewAdapter adapter;
    private boolean mustBeRefreshPosts = false;

    private Uri photoURI;
    private File photoFile;

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

    private void CheckEditMode() {

        // handle edit mode
        if (getIntent().hasExtra(UPDATE_POST_KEY)) {
            isEditMode = true;

            featurePost = getIntent().getParcelableExtra(UPDATE_POST_KEY);

            // text
            text.setText(featurePost.getDescription());
            text.setSelection(featurePost.getDescription().length());

            // user image
            if (featurePost.getUser() != null && featurePost.getUser().getImage_url() != null && !featurePost.getUser().getImage_url().isEmpty())
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

            HomeScreenMediaChooser.startMediaChooser(this, false);

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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        CharSequence[] languages = new CharSequence[2];
                        languages[0] = "from gallery";
                        languages[1] = "from camera";

                        builder.setItems(languages, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        openGallery();
                                        break;
                                    case 1:
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                        //creating file for camera photo
                                        photoFile = null;
                                        try {
                                            photoFile = createCameraTempPhotoFile();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                        if (photoFile != null && takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            photoURI = FileProvider.getUriForFile(CreatePostActivity.this,
                                                    //"ai.medicus.android.fileprovider",
                                                    "com.gnusl.acton" + ".fileprovider",
                                                    photoFile);

                                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        AlertDialog changeLangsDialog = builder.create();
                        changeLangsDialog.setCancelable(true);
                        changeLangsDialog.setCanceledOnTouchOutside(true);
                        changeLangsDialog.show();
                    }
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        GALLERY_PERMISSIONS_REQUEST);
            }
        });

    }

    public static File createCameraTempPhotoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = WowApplication.getApplicationInstance().getFilesDir();
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            File file = new File(fileUri.getPath());
//            handleMediaCapture(file);
            handleMediaCapture(photoFile);

        }
    }

    private void handleMediaCapture(File mediaFile) {

        adapter.addAll(mediaFile.getPath());
        adapter.notifyDataSetChanged();

    }

    private void shareYourPost() {

        if (text.getText().toString().isEmpty())
            Toast.makeText(this, getString(R.string.create_post_hint), Toast.LENGTH_SHORT).show();

        else {

            ArrayList<File> files = adapter.getImagesAsFiles();

            if (!files.isEmpty()) {

                // make progress dialog
                LoaderPopUp.show(this);

                // upload image
                APIConnectionNetwork.UploadImage(adapter.getImagesAsFiles().get(0), this);

            } else
                sendUploadPostRequest(null);

        }

    }

    private void sendUploadPostRequest(String imageName) {

        if (!isEditMode) {

            // make progress dialog
            LoaderPopUp.show(this);

            // send request
            APIConnectionNetwork.CreateNewPost(text.getText().toString(), imageName, this);

        } else {

            // make progress dialog
            LoaderPopUp.show(this);

            // send request
            APIConnectionNetwork.UpdatePost(featurePost.getId(), text.getText().toString(), this);

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
        MediaChooser.showCameraVideoView(true);
        MediaChooser.setSelectionLimit(1);
        MediaChooser.showOnlyImageTab();

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

        } else if (jsonObject.has("success")) { // update post

            Toast.makeText(this, getString(R.string.success_updating), Toast.LENGTH_SHORT).show();
            mustBeRefreshPosts = true;
            finish();

        } else if (jsonObject.has("image")) {

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
                        HomeScreenMediaChooser.startMediaChooser(this, false);

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

    public static void launch(Activity activity) {

        Intent intent = new Intent(activity, CreatePostActivity.class);
        activity.startActivity(intent);
    }

    public static void launchToEdit(Activity activity, FeaturePost post) {

        Intent intent = new Intent(activity, CreatePostActivity.class);
        intent.putExtra(CreatePostActivity.UPDATE_POST_KEY, post);
        activity.startActivity(intent);
    }
}
