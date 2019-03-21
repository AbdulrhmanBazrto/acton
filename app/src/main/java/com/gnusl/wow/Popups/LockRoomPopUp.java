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

public class LockRoomPopUp extends DialogFragment implements ConnectionDelegate {

    private Room room;
    private RoomLockType roomLockType;
    private EditText password;
    private EditText confirm_password;
    private LockRoomPopUpDelegate lockRoomPopUpDelegate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setCancelable(true);


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

        View dialogView = inflater.inflate(R.layout.popup_send_message_layout, container);

        password = dialogView.findViewById(R.id.password);
        confirm_password = dialogView.findViewById(R.id.confirm_password);

        dialogView.findViewById(R.id.save_password).setOnClickListener(v -> clickSave());

        return dialogView;
    }

    private void clickSave() {

        if (password.getText() == null || password.getText().toString().isEmpty())
            password.setError(getString(R.string.password_is_required));

        else if (confirm_password.getText() == null || confirm_password.getText().toString().isEmpty())
            confirm_password.setError(getString(R.string.please_confirm_your_password));

        else if (!password.getText().toString().equals(confirm_password.getText().toString()))
            confirm_password.setError(getString(R.string.your_confirm_not_match));

        else
            sendRoomPassword();

    }

    private void sendRoomPassword(){

        // show progress
        LoaderPopUp.show(getActivity());

        // save password
        APIConnectionNetwork.SetRoomPassWord(password.getText().toString(),getRoom().getId(),getRoomLockType().getId(),this);
    }

    public static void show(FragmentActivity activity,Room room,RoomLockType roomLockType,LockRoomPopUpDelegate delegate) {

        LockRoomPopUp lockRoomPassword = new LockRoomPopUp();
        lockRoomPassword.setRoom(room);
        lockRoomPassword.setRoomLockType(roomLockType);
        lockRoomPassword.setLockRoomPopUpDelegate(delegate);
        lockRoomPassword.show(activity.getSupportFragmentManager(), "");

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

        dismiss();

        getLockRoomPopUpDelegate().onSetPasswordSuccess(getRoomLockType());
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }

    public RoomLockType getRoomLockType() {
        return roomLockType;
    }

    public void setRoomLockType(RoomLockType roomLockType) {
        this.roomLockType = roomLockType;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LockRoomPopUpDelegate getLockRoomPopUpDelegate() {
        return lockRoomPopUpDelegate;
    }

    public void setLockRoomPopUpDelegate(LockRoomPopUpDelegate lockRoomPopUpDelegate) {
        this.lockRoomPopUpDelegate = lockRoomPopUpDelegate;
    }
}
