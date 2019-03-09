package com.gnusl.wow.Popups;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.gnusl.wow.Adapters.GiftsRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersSpinnerAdapter;
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class GiftsRoomDialog {

    public static boolean isOpened = false;

    public static void show(final Context context, ArrayList<Gift> gifts, List<User> users, GiftDelegate giftDelegate) {
        if (isOpened) return;
        isOpened = true;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.gifts_room_dialog_layout, null);

        RecyclerView recyclerView = dialogView.findViewById(R.id.gifts_recycler_view);
        Spinner spinner = dialogView.findViewById(R.id.users_spinner);

        List<Object> objects = new ArrayList<>();
        objects.add("select user");
        objects.addAll(users);
        UsersSpinnerAdapter usersSpinnerAdapter = new UsersSpinnerAdapter(context, R.layout.user_drop_down_item, objects);
        spinner.setAdapter(usersSpinnerAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        final BottomSheetDialog optionsDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);

        GiftsRecyclerViewAdapter giftsRecyclerViewAdapter = new GiftsRecyclerViewAdapter(context, gifts, optionsDialog, giftDelegate, spinner, users);
        recyclerView.setAdapter(giftsRecyclerViewAdapter);

        optionsDialog.contentView(dialogView)
                .heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                .inDuration(500)
                .cancelable(true)
                .show();

        isOpened = false;

    }
}
