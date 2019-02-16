package com.gnusl.wow.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.view.View;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Fragments.RoomSettingsFragment;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.gnusl.wow.WebRtcClient.PermissionChecker;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;

public class UserDetailsActivity extends AppCompatActivity implements ConnectionDelegate, DatePickerDialog.OnDateSetListener {

    private ImageView userImage;
    private TextView userName;
    private TextView userBirthdateTv;
    private TextView userSixTv;
    BroadcastReceiver imageBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // initialize views
        findViews();

        // initialize media
        InitializeMediaPicker();

        // refresh user details
        refreshUserDetails(SharedPreferencesUtils.getUser());
    }

    private void findViews(){

        userImage=findViewById(R.id.profile_image);
        userName=findViewById(R.id.user_name_tv);
        userBirthdateTv=findViewById(R.id.user_birthdate_tv);
        userSixTv=findViewById(R.id.user_six_tv);

        // back
        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        findViewById(R.id.user_image_section).setOnClickListener(v->{

            openGallery();
        });

        findViewById(R.id.room_name_layout).setOnClickListener(v->{

            // show popup
            new MaterialDialog.Builder(this)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("أدخل الأسم","", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            // show progress
                            LoaderPopUp.show(UserDetailsActivity.this);

                            // show in text
                            userName.setText(input.toString());

                            // send request
                            APIConnectionNetwork.UpdateUserName(input.toString(),UserDetailsActivity.this);
                        }
                    }).show();
        });

        // show date picker
        findViewById(R.id.room_lock_layout).setOnClickListener(v->{

            showDatePicker();
        });


        // show sex dialog
        findViewById(R.id.sex_layout).setOnClickListener(v->{

            // show popup
            new MaterialDialog.Builder(this)
                    .items(R.array.sex_array)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            // show text
                            userSixTv.setText(text);

                            // show progress
                            LoaderPopUp.show(UserDetailsActivity.this);

                            // send request
                            APIConnectionNetwork.UpdateUserGender(text.toString(),UserDetailsActivity.this);
                        }
                    })
                    .show();

        });


    }

    private void refreshUserDetails(User user){

        if(user==null)
            return;

        // load image
        if (user.getImage_url() != null && !user.getImage_url().isEmpty())
            Glide.with(this)
                    .load(user.getImage_url())
                    .into(userImage);

        // name
        userName.setText(user.getName());

        // birthdate
        userBirthdateTv.setText(user.getBirthday());

        // gender
        userSixTv.setText(user.getGender());
    }

    private void InitializeMediaPicker() {

        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(UserDetailsActivity.this, "yippiee Image ", Toast.LENGTH_SHORT).show();
                Toast.makeText(UserDetailsActivity.this, "Image SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();

                // change room backround
                changeUserImage(intent.getStringArrayListExtra("list"));

            }
        };

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        MediaChooser.showCameraVideoView(false);

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

    private void changeUserImage(List<String> filePathList) {

        // create file image
        File mediaFile = new File(filePathList.get(0));

        if (!(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp"))) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            options.inSampleSize = 2;
            Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
            userImage.setImageBitmap(myBitmap);
        }

        // TODO : upload image
        // make progress dialog
        LoaderPopUp.show(this);

        // upload image
        APIConnectionNetwork.UploadImage(mediaFile, this);

    }

    private void showDatePicker(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(UserDetailsActivity.this, R.style.Theme_DatePickerTheme, this, 1975, 0, 1);

        Calendar minAdultAge = new GregorianCalendar();
        datePickerDialog.getDatePicker().setMaxDate(minAdultAge.getTimeInMillis());
        datePickerDialog.show();

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
    public void onConnectionFailure() {

        Toast.makeText(this, "failure response..", LENGTH_LONG).show();

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

        if (jsonObject.has("image")) {

            // upload user image
            try {
                APIConnectionNetwork.UpdateUserImage(jsonObject.getString("image"), this);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return;
        }


        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parse user updated
        if(jsonArray.length()>0){
            try {
                User user=User.newInstance(jsonArray.getJSONObject(0));

                // refresh user
                refreshUserDetails(user);

                // save user
                SharedPreferencesUtils.saveUser(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int day) {

        String dateString=String.valueOf(year+"-"+(monthOfYear+1)+"-"+day);

        // show date
        userBirthdateTv.setText(dateString);

        // show progress
        LoaderPopUp.show(UserDetailsActivity.this);

        // update user
        APIConnectionNetwork.UpdateUserBirthDate(dateString,this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(imageBroadcastReceiver);
        super.onDestroy();
    }
}
