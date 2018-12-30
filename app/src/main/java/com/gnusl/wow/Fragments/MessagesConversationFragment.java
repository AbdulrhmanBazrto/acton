package com.gnusl.wow.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.MessagesConversationRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.MessageImageDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.APIUtils;
import com.gnusl.wow.Utils.NetworkUtils;
import com.gnusl.wow.Views.FontedEditText;
import com.gnusl.wow.Views.FontedTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static com.gnusl.wow.Utils.PermissionsUtils.CALENDAR_PERMISSION;
import static com.gnusl.wow.Utils.PermissionsUtils.CAMERA_PERMISSIONS_REQUEST;
import static com.gnusl.wow.Utils.PermissionsUtils.checkCameraPermissions;

public class MessagesConversationFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener, MessageImageDelegate {

    private View inflatedView;
    private RecyclerView recyclerView;
    private MessagesConversationRecyclerViewAdapter messagesConversationAdapter;
    LinearLayoutManager layoutManager;
    private RelativeLayout toolbar_layout;
    private FontedEditText message_edit_text;
    private FontedTextView state_label;
    private AppCompatImageView send_button, photo_button, bigImage;
    private int offset = 0;
    private int buffOffset = 0;
    private boolean isNotMoreMessages = false;
    private boolean isLockToRefresh = true;
    private int user_id;

    private ProgressDialog progressDialog;

    @SuppressLint("ValidFragment")
    public MessagesConversationFragment(int user_id) {

        this.user_id = user_id;
    }

    public static MessagesConversationFragment newInstance(int user_id) {

        return new MessagesConversationFragment(user_id);
    }


    public MessagesConversationFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_messages_conversation_layout, container, false);

        recyclerView = (RecyclerView) inflatedView.findViewById(R.id.recycler_view_messeges_conversation);
        message_edit_text = (FontedEditText) inflatedView.findViewById(R.id.message_edit_text);
        send_button = (AppCompatImageView) inflatedView.findViewById(R.id.send_button);
        state_label = (FontedTextView) inflatedView.findViewById(R.id.state_label);
        // photo_button = inflatedView.findViewById(R.id.photo_button);
        bigImage = inflatedView.findViewById(R.id.bigImage);

        // init adapter
        setUpRecyclerView();

        // send message
        send_button.setOnClickListener(v -> handleClickSendMessage());

        // photo_button.setOnClickListener(v -> handleClickSendPhoto());

        sendMessagesRequest();

        return inflatedView;
    }

    private void handleClickSendPhoto() {
      /*  if (checkCameraPermissions(getActivity())) {

            showProfilePictureChooserDialog(getActivity());

        } else {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    CAMERA_PERMISSIONS_REQUEST);

        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        if (NetworkUtils.getInstance(getContext()).isOnline()) {
            state_label.setVisibility(View.GONE);

        } else {
            state_label.setVisibility(View.VISIBLE);
        }

        messagesConversationAdapter = new MessagesConversationRecyclerViewAdapter(getContext(), new ArrayList<>(), recyclerView, this);
        messagesConversationAdapter.setDelegate(this);

        recyclerView.setAdapter(messagesConversationAdapter);
        increaseOffset();

           /* try {
                recyclerView.smoothScrollToPosition(messagesConversationAdapter.getItemCount() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
    }

    private void sendMessagesRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading messeges..");

        isLockToRefresh = true;

        // send request
        APIConnectionNetwork.GetMessagesByUser(user_id, this);
    }

    private void increaseOffset() {

        offset += buffOffset;
    }

    private void refreshMessages() {

        if (messagesConversationAdapter != null) {

            messagesConversationAdapter.setLoading(true);

            if (isNotMoreMessages) {

                if (getContext() != null)
                    Toast.makeText(getContext(), "no messages more..", LENGTH_LONG).show();

                new Handler().postDelayed(() -> {
                    // stop loading
                    messagesConversationAdapter.setLoading(false);
                }, 2000);

                return;

            } else
                new Handler().postDelayed(() -> {
                    // stop loading
                    updateMessagesAdapter();
                }, 1000);

        }
    }

    private void updateAdapterWithError() {

        if (messagesConversationAdapter != null)
            // stop loading
            messagesConversationAdapter.setLoading(false);
    }

    private void updateMessagesAdapter() {

        // stop loading
        messagesConversationAdapter.setLoading(false);

        try {
            recyclerView.smoothScrollToPosition(messagesConversationAdapter.getItemCount() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        increaseOffset();

    }

    private void handleClickSendMessage() {

        if (!message_edit_text.getText().toString().isEmpty()) {

            message_edit_text.setText("");
            isLockToRefresh = true;

            APIConnectionNetwork.SendMessageToUser(message_edit_text.getText().toString(), user_id, this);
        }

    }

    private void updateStatusAfterSendMessage() {

        if (getContext() != null)
            Toast.makeText(getContext(), "send message successfuly", LENGTH_LONG).show();

        // clear message text
        message_edit_text.setText("");

        // send request
        isLockToRefresh = true;
        APIConnectionNetwork.GetMessagesByUser(user_id, this);
    }

    @Override
    public void onConnectionFailure() {

        isNotMoreMessages = true;
        updateAdapterWithError();

        isLockToRefresh = false;

        if (getContext() != null)
            Toast.makeText(getContext(), "no messages more..", LENGTH_LONG).show();

        if (progressDialog != null)
            progressDialog.dismiss();


    }

    @Override
    public void onConnectionError(ANError anError) {

        if (getContext() != null)
            Toast.makeText(getContext(), "Error Connection try again", LENGTH_LONG).show();

        isLockToRefresh = false;

        if (progressDialog != null)
            progressDialog.dismiss();

        updateAdapterWithError();
    }

    @Override
    public void onConnectionSuccess(String response) {

        // send message response
        if (progressDialog != null)
            progressDialog.dismiss();


        isLockToRefresh = false;

        // send message
        if (response.equalsIgnoreCase("true"))
            updateStatusAfterSendMessage();

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        // sync messages
        if (jsonObject.has("status")) {
            try {
                if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    APIConnectionNetwork.GetMessagesByUser(user_id, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // sync messages
        if (progressDialog != null)
            progressDialog.dismiss();

        isLockToRefresh = false;

        // handle offset
        buffOffset = jsonArray.length();

        // update posts adapter
        //updateMessagesAdapter();

        // parsing
        ArrayList<Message> messages = Message.parseJSONArray(jsonArray);

        messagesConversationAdapter.setMessages(messages);
        messagesConversationAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoadMore() {

        if (!isLockToRefresh)
            refreshMessages();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* if (resultCode == RESULT_OK)
            switch (requestCode) {

                case PICK_IMAGE_REQUEST_CODE:
                    Log.d("profilePicture", "onActivityResult: PICK_IMAGE_PROFILE_REQUEST_CODE..");
                    Uri galleryPictureUri = data.getData();
                    Log.d("profilePicture", "onActivityResult: uri: " + galleryPictureUri.toString());

                    Log.d("profilePicture", "onActivityResult: delegate isn't null");
                    startCrop(getActivity(), galleryPictureUri);

                    break;

                case CAMERA_REQUEST_CODE:
                    Log.d("profilePicture", "onActivityResult: CAMERA_PROFILE_REQUEST_CODE..");

                    Uri cameraPictureUri = MediaUtils.photoURI; //when you put extra to intent the response data is null and you should use the same URI you have passed

                    Log.d("profilePicture", "onActivityResult: profileActionsDelegate not null, uri is: " + cameraPictureUri);
                    startCrop(getActivity(), cameraPictureUri);

                    break;

                case UCrop.REQUEST_CROP:
                    Log.d("profilePicture", "onActivityResult: REQUEST_CROP started..");

                    final Uri resultUri = UCrop.getOutput(data);
                    Log.d("profilePicture", "onActivityResult: uri: " + resultUri);

                    try {

                        Bitmap imageBitmap = SiliCompressor.with(getActivity()).getCompressBitmap(resultUri.toString());
                        Log.d("profilePicture", "onActivityResult: bitmap filled..");
                        ConnectionAPINetwork.sendImageMessage(ImageUtils.ToBase64ImageString(imageBitmap), user_id, this);

                    } catch (IOException e) {

                        Log.d("profilePicture", "onActivityResult: exception thrown " + e.toString());
                        e.printStackTrace();

                    }

                    // get bitmap
//                    selected_image=MediaUtils.userTakeImage(null,resultUri);

                    Log.d("requestCode", "onActivityResult: there is an error resultCode is: " + resultCode);
                    break;

            }
        else {
            Log.d("requestCode", "onActivityResult: there is an error resultCode is: " + resultCode);
        }*/

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != CALENDAR_PERMISSION) {


            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0) {
                if (grantResults.length == 1) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && !permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR)) {

                        //Coming from profile
                        showProfilePictureChooserDialog(getActivity());

                    } else {
                        Toast.makeText(getActivity(), R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                    }

                } else if (grantResults.length == 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && !permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR)) {

                        showProfilePictureChooserDialog(getActivity());

                    } else if (!permissions[0].equals(Manifest.permission.READ_CALENDAR) && !permissions[0].equals(Manifest.permission.WRITE_CALENDAR))
                        Toast.makeText(getActivity(), R.string.grant_equired_permissions_please, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }


    public static final int PICK_IMAGE_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static Uri photoURI;

    private void openGallery() {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, PICK_IMAGE_REQUEST_CODE);
    }

    private void openCamera() {

       /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //creating file for camera photo
        File photoFile = null;
        try {
            photoFile = ImageUtils.createCameraTempPhotoFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (photoFile != null &&
                takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            photoURI = FileProvider.getUriForFile(getActivity(),
                    //"ai.medicus.android.fileprovider",
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }*/
    }

    public void startCrop(Activity activity, Uri uri) {

       /* Log.d("profilePicture", "startCrop started..");

        Uri outputUri;
        outputUri = Uri.fromFile(new File(activity.getCacheDir(), "cropped"));

        UCrop uCrop = UCrop.of(uri, outputUri);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimary));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(activity, R.color.white));
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, true);
        uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "Crop image");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            uCrop.getIntent(activity).putExtra(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(activity, R.color.colorPrimary));
        }
        uCrop.start(getActivity(), this, UCrop.REQUEST_CROP);*/
    }

    public void showProfilePictureChooserDialog(Activity activity) {
       /* final String[] items = new String[]{activity.getString(R.string.from_gallery), activity.getString(R.string.from_camera)};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.camera_or_gallery_dialog_item, items);

        new AlertDialog.Builder(activity).setTitle(R.string.select_source)
                .setAdapter(adapter, (dialog, item) -> {

                    if (item == 0) {
                        openGallery();
                    } else if (item == 1) {
                        openCamera();
                    }

                })
                .show();*/
    }


    @Override
    public void openImages(Bitmap image) {
       /* if (bigImage.getVisibility() == View.GONE){
            bigImage.setVisibility(View.VISIBLE);
            bigImage.setImageBitmap(image);
            }*/
    }

   /* @Subscribe
    public void onReceiveMessageEvent(ReceiveMessageEvent receiveMessageEvent) {
        if (receiveMessageEvent.getMessage() != null) {
            Log.i("MessageSection Received ", receiveMessageEvent.getMessage().getMessage());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    buffOffset++;
                    List<Message> mm = new ArrayList<>();
                    mm.add(receiveMessageEvent.getMessage());
                    messagesConversationAdapter.update(mm);
                    try {
                        recyclerView.smoothScrollToPosition(messagesConversationAdapter.getItemCount() - 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        // EventBus.getDefault().unregister(this);
    }

}
