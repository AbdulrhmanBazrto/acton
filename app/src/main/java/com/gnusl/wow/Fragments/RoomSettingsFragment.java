package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Activities.SettingsActivity;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class RoomSettingsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private RoomSettingsActivity activity;

    private ProgressDialog progressDialog;
    private TextView roomNameTv;

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
                            progressDialog = ProgressDialog.show(getContext(), "", "change room name..");

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

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "error Connection try again", LENGTH_LONG).show();

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
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }
}
