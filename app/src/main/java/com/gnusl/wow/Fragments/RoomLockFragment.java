package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Adapters.RoomLockTypeAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.LockRoomPopUpDelegate;
import com.gnusl.wow.Delegates.SelectLockTypeDelegate;
import com.gnusl.wow.Models.RoomLockType;
import com.gnusl.wow.Popups.LockRoomPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class RoomLockFragment extends Fragment implements ConnectionDelegate, SelectLockTypeDelegate, LockRoomPopUpDelegate {

    private View inflatedView;
    private RoomSettingsActivity activity;

    private ProgressDialog progressDialog;
    private TextView roomNameTv;
    private RoomLockTypeAdapter roomLockTypeAdapter;

    public RoomLockFragment() {
    }

    public static RoomLockFragment newInstance() {

        return new RoomLockFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room_lock, container, false);

        // views
        findViews();

        // init adapter
        initializeLockTypeAdapter();

        // set request
        getAllLockTypes();

        return inflatedView;
    }

    private void findViews(){


    }

    private void initializeLockTypeAdapter() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.room_lock_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomLockTypeAdapter= new RoomLockTypeAdapter(getContext(), new ArrayList<>(),this);
        recyclerView.setAdapter(roomLockTypeAdapter);
    }

    private void getAllLockTypes(){

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "get Room Lock Types..");

        // send request
        APIConnectionNetwork.GetRoomLockType(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof RoomSettingsActivity)
            activity= (RoomSettingsActivity) context;

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

        // parsing
        ArrayList<RoomLockType> rooms=RoomLockType.parseJSONArray(jsonArray);

        // notify
        roomLockTypeAdapter.setRoomTypes(rooms);
        roomLockTypeAdapter.notifyDataSetChanged();

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onSelectRoomLockType(RoomLockType roomLockType) {

        // show popup
        LockRoomPopUp.show(getActivity(),activity.getRoom(),roomLockType,this);
    }

    @Override
    public void onSetPasswordSuccess(RoomLockType roomLockType) {

        ((TextView)inflatedView.findViewById(R.id.month_value)).setText(roomLockType.getName());

    }
}