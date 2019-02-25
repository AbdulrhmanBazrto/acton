package com.gnusl.wow.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.CommentsPostActivity;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Activities.SettingsActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.PermissionsUtils;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.GALLERY_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkReadGalleryPermissions;

public class RoomSettingsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private RoomSettingsActivity activity;
    private TextView roomNameTv;

    private boolean isUploadingForBackground = false;
    BroadcastReceiver imageBroadcastReceiver;

    public RoomSettingsFragment() {
    }

    public static RoomSettingsFragment newInstance() {

        return new RoomSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room_settings, container, false);

        // views
        findViews();
        InitializeMediaPicker();

        return inflatedView;
    }

    private void findViews() {

        roomNameTv = inflatedView.findViewById(R.id.room_name_tv);

        roomNameTv.setText(activity.getRoom().getName());

        inflatedView.findViewById(R.id.room_name_layout).setOnClickListener(v -> {

            // show popup
            new MaterialDialog.Builder(getContext())
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("أدخل اسم الغرفة", activity.getRoom().getName(), new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            // show progress
                            LoaderPopUp.show(getActivity());

                            // show in text
                            roomNameTv.setText(input.toString());

                            // send request
                            APIConnectionNetwork.ChangeRoomName(input.toString(), activity.getRoom().getId(), RoomSettingsFragment.this);
                        }
                    }).show();
        });


        inflatedView.findViewById(R.id.room_lock_layout).setOnClickListener(v -> {

            activity.makeFragment(FragmentTags.RoomLockFragment);
        });

        inflatedView.findViewById(R.id.profile_image).setOnClickListener(v -> {
            openGallery();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RoomSettingsActivity)
            activity = (RoomSettingsActivity) context;

    }

    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), "failure response..", LENGTH_LONG).show();

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "error Connection try again", LENGTH_LONG).show();

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    public void openGallery() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            MediaChooser.showOnlyImageTab();
            HomeScreenMediaChooser.startMediaChooser(getActivity(), true);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSIONS_REQUEST);
        }

    }

    private void handleMediaReceiver(List<String> filePathList) {

        // create file image
        File mediaFile = new File(filePathList.get(0));

        if (!(mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp"))) {
        }

        // TODO : upload image
        // make progress dialog
        LoaderPopUp.show(getActivity());

        // upload image
        APIConnectionNetwork.UploadImage(mediaFile, new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionError(ANError anError) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(String response) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
            }
        });

    }

    private void InitializeMediaPicker() {

      /*  videoBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(RoomChatActivity.this, "yippiee Video ", Toast.LENGTH_SHORT).show();
                Toast.makeText(RoomChatActivity.this, "Video SIZE :" + intent.getStringArrayListExtra("list").size(), Toast.LENGTH_SHORT).show();
                //setAdapter(intent.getStringArrayListExtra("list"));
            }
        };*/


        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                // change room backround
                handleMediaReceiver(intent.getStringArrayListExtra("list"));

            }
        };


        //  IntentFilter videoIntentFilter = new IntentFilter(MediaChooser.VIDEO_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        //registerReceiver(videoBroadcastReceiver, videoIntentFilter);

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        getActivity().registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        MediaChooser.showCameraVideoView(false);

    }
}
