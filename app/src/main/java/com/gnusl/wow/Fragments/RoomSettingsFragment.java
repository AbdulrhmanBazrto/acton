package com.gnusl.wow.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Adapters.HashTagRecyclerViewAdapter;
import com.gnusl.wow.Application.WowApplication;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.SelectHashTagDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Models.ActivitiesHashTag;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.R;
import com.learnncode.mediachooser.MediaChooser;
import com.learnncode.mediachooser.activity.HomeScreenMediaChooser;
import com.squareup.picasso.Picasso;

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
import static com.learnncode.mediachooser.Utilities.MediaChooserConstants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

public class RoomSettingsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private RoomSettingsActivity activity;
    private TextView roomNameTv, tvFees;
    private ImageView ivProfileImage;

    private boolean isUploadingForBackground = false;
    BroadcastReceiver imageBroadcastReceiver;

    ActivitiesHashTag selectedHashTag = null;
    private Uri photoURI;
    private File photoFile;

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
        tvFees = inflatedView.findViewById(R.id.tv_fees);

        roomNameTv.setText(activity.getRoom().getName());
        tvFees.setText(activity.getRoom().isFree() ? "0" : String.valueOf(activity.getRoom().getSubscriptionPrice()));

        //set room tag
        ((TextView) inflatedView.findViewById(R.id.tv_tag)).setText(activity.getRoom().getTag());

        inflatedView.findViewById(R.id.room_name_layout).setOnClickListener(v -> {

            // show popup
            new MaterialDialog.Builder(getContext())
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(getString(R.string.enter_room_name), activity.getRoom().getName(), new MaterialDialog.InputCallback() {
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

        inflatedView.findViewById(R.id.room_status_layout).setOnClickListener(v -> {

            // show popup
            new MaterialDialog.Builder(getContext())
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(getString(R.string.enter_public_notification), activity.getRoom().getDescription(), new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            // show progress
                            LoaderPopUp.show(getActivity());
                            activity.getRoom().setDescription(input.toString());
                            // send request
                            APIConnectionNetwork.ChangeRoomDescription(input.toString(), activity.getRoom().getId(), RoomSettingsFragment.this);
                        }
                    }).show();
        });

        inflatedView.findViewById(R.id.cl_fees).setOnClickListener(v -> {

            // show popup
            new MaterialDialog.Builder(getContext())
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(getString(R.string.enter_entrence_fee), activity.getRoom().isFree() ? "0" : String.valueOf(activity.getRoom().getSubscriptionPrice()), new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            // show progress
                            LoaderPopUp.show(getActivity());
                            tvFees.setText(input.toString());

                            if (input.toString().equalsIgnoreCase("0"))
                                activity.getRoom().setFree(true);
                            else {
                                activity.getRoom().setFree(false);
                                activity.getRoom().setSubscriptionPrice(Double.parseDouble(input.toString()));
                            }

                            // send request
                            APIConnectionNetwork.ChangeRoomPrice(input.toString(), activity.getRoom().getId(), RoomSettingsFragment.this);
                        }
                    }).show();
        });

        inflatedView.findViewById(R.id.room_hashTag_layout).setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getActivity(), R.style.PopupStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null)
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.layout_hashtag);
            dialog.show();

            RecyclerView rvHashTags = dialog.findViewById(R.id.rv_hashTags);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvHashTags.setLayoutManager(linearLayoutManager);


            HashTagRecyclerViewAdapter hashTagRecyclerViewAdapter = new HashTagRecyclerViewAdapter(getActivity(), new ArrayList<>(), activity.getRoom().getTag(), new SelectHashTagDelegate() {
                @Override
                public void onSelect(ActivitiesHashTag activitiesHashTag) {
                    selectedHashTag = activitiesHashTag;
                }
            });
            rvHashTags.setAdapter(hashTagRecyclerViewAdapter);

            dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedHashTag != null) {
                        APIConnectionNetwork.ChangeRoomTag(selectedHashTag.getActivity(), activity.getRoom().getId(), null);
                        dialog.dismiss();
                        ((TextView) inflatedView.findViewById(R.id.tv_tag)).setText(selectedHashTag.getActivity());
                        activity.getRoom().setTag(selectedHashTag.getActivity());
                    }
                }
            });

            APIConnectionNetwork.GetExploreContent(new ConnectionDelegate() {
                @Override
                public void onConnectionFailure() {

                }

                @Override
                public void onConnectionError(ANError anError) {

                }

                @Override
                public void onConnectionSuccess(String response) {

                }

                @Override
                public void onConnectionSuccess(JSONObject jsonObject) {
                    if (jsonObject.has("tags")) {
                        try {
                            JSONArray tagsJsonArray = jsonObject.getJSONArray("tags");
                            ArrayList<ActivitiesHashTag> hashTags = new ArrayList<>();
                            for (int i = 0; i < tagsJsonArray.length(); i++)
                                hashTags.add(new ActivitiesHashTag(tagsJsonArray.getString(i)));
                            hashTagRecyclerViewAdapter.setActivities(hashTags);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onConnectionSuccess(JSONArray jsonArray) {

                }
            });

        });


        inflatedView.findViewById(R.id.room_lock_layout).setOnClickListener(v -> {

            activity.makeFragment(FragmentTags.RoomLockFragment);
        });

        ivProfileImage = inflatedView.findViewById(R.id.profile_image);
        ivProfileImage.setOnClickListener(v -> {
            openGallery();
        });

        if (activity.getRoom().getThumbnailUrl() != null && !activity.getRoom().getThumbnailUrl().equalsIgnoreCase(""))
            Picasso.with(getActivity()).load(activity.getRoom().getThumbnailUrl()).into(ivProfileImage);

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

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    CharSequence[] languages = new CharSequence[2];
                    languages[0] = "from gallery";
                    languages[1] = "from camera";

                    builder.setItems(languages, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    HomeScreenMediaChooser.startMediaChooser(getActivity(), true);
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

                                    if (photoFile != null && takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                                        photoURI = FileProvider.getUriForFile(activity,
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    GALLERY_PERMISSIONS_REQUEST);
        }

    }

    private void handleMediaReceiver(List<String> filePathList) {

        // create file image
        File mediaFile = new File(filePathList.get(0));

        if ((mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") ||
                mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp"))) {
            Toast.makeText(getActivity(), "can't choose video, please choose a photo", LENGTH_LONG).show();
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 2;
        Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
        ivProfileImage.setImageBitmap(myBitmap);
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
                APIConnectionNetwork.ChangeRoomPicture(jsonObject.optString("image"), activity.getRoom().getId(), null);
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
            }
        });

    }

    private void handleMediaCapture(File mediaFile) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = 2;
        Bitmap myBitmap = BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options);
        ivProfileImage.setImageBitmap(myBitmap);
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
                APIConnectionNetwork.ChangeRoomPicture(jsonObject.optString("image"), activity.getRoom().getId(), null);
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {
                LoaderPopUp.dismissLoader();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null)
            getActivity().unregisterReceiver(imageBroadcastReceiver);
    }

    private void InitializeMediaPicker() {

        imageBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                handleMediaReceiver(intent.getStringArrayListExtra("list"));

            }
        };

        IntentFilter imageIntentFilter = new IntentFilter(MediaChooser.IMAGE_SELECTED_ACTION_FROM_MEDIA_CHOOSER);
        getActivity().registerReceiver(imageBroadcastReceiver, imageIntentFilter);
        MediaChooser.showCameraVideoView(false);
        MediaChooser.showOnlyImageTab();

    }

//    private Uri getOutputMediaFileUri(Context context, int type) {
//        File outputMediaFile = getOutputMediaFile(type);
//        if (outputMediaFile != null)
//            return FileProvider.getUriForFile(context,
//                    "com.gnusl.acton" + ".fileprovider",
//                    outputMediaFile);
//        else
//            return null;
//    }

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
}
