package com.gnusl.wow.Fragments;

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
import com.gnusl.wow.Activities.RoomLockSettingActivity;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Adapters.RoomLockTypeAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.LockRoomPopUpDelegate;
import com.gnusl.wow.Delegates.SelectLockTypeDelegate;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.RoomLockType;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.Popups.LockRoomPopUp;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class StoreRoomLockFragment extends Fragment implements ConnectionDelegate, SelectLockTypeDelegate, LockRoomPopUpDelegate {

    private View inflatedView;
    private TextView roomNameTv;
    Room room = new Room();
    private RoomLockTypeAdapter roomLockTypeAdapter;

    public StoreRoomLockFragment() {
    }

    public static StoreRoomLockFragment newInstance() {

        return new StoreRoomLockFragment();
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

    private void findViews() {


    }

    private void initializeLockTypeAdapter() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.room_lock_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        roomLockTypeAdapter = new RoomLockTypeAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(roomLockTypeAdapter);
    }

    private void getAllLockTypes() {

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetRoomLockType(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
        room.setId(Integer.parseInt(response));
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<RoomLockType> rooms = RoomLockType.parseJSONArray(jsonArray);

        // notify
        roomLockTypeAdapter.setRoomTypes(rooms);
        roomLockTypeAdapter.notifyDataSetChanged();

        // dismiss
        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onSelectRoomLockType(RoomLockType roomLockType) {

        // show popup
        LockRoomPopUp.show(getActivity(), room, roomLockType, this);

    }

    @Override
    public void onSetPasswordSuccess(RoomLockType roomLockType) {

        ((TextView) inflatedView.findViewById(R.id.month_value)).setText(roomLockType.getName());

    }
}