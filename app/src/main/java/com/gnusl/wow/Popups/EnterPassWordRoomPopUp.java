package com.gnusl.wow.Popups;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.LockRoomPopUpDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.RoomLockType;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class EnterPassWordRoomPopUp extends DialogFragment {

    private Room room;
    private EditText password;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setCancelable(false);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Window window = getDialog().getWindow();

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View dialogView = inflater.inflate(R.layout.popup_enter_password_layout, container);

        password = dialogView.findViewById(R.id.password);

        dialogView.findViewById(R.id.save_password).setOnClickListener(v -> clickSave());
        dialogView.findViewById(R.id.cancel_password).setOnClickListener(v -> dismissPopup());

        return dialogView;
    }

    private void clickSave() {

        if (!password.getText().toString().equalsIgnoreCase(getRoom().getPassword()))
            password.setError(getString(R.string.incorrect_password));
        else
            dismiss();

    }

    private void dismissPopup() {

        dismiss();

        getActivity().finish();
    }


    public static void show(FragmentActivity activity, Room room) {

        EnterPassWordRoomPopUp enterPassWordRoomPopUp = new EnterPassWordRoomPopUp();
        enterPassWordRoomPopUp.setRoom(room);
        enterPassWordRoomPopUp.show(activity.getSupportFragmentManager(), "");

    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}

