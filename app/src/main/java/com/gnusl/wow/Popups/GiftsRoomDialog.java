package com.gnusl.wow.Popups;

import android.content.Context;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Adapters.GiftsRecyclerViewAdapter;
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.R;
import com.gnusl.wow.Views.FontedTextView;
import com.rey.material.app.BottomSheetDialog;

import java.util.ArrayList;

public class GiftsRoomDialog {

    public static boolean isOpened = false;

    public static void show(final Context context, ArrayList<Gift> gifts, GiftDelegate giftDelegate)  {
        if (isOpened) return;
        isOpened = true;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.gifts_room_dialog_layout, null);

        RecyclerView recyclerView=dialogView.findViewById(R.id.gifts_recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        final BottomSheetDialog optionsDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);

        GiftsRecyclerViewAdapter giftsRecyclerViewAdapter=new GiftsRecyclerViewAdapter(context,gifts,optionsDialog,giftDelegate);
        recyclerView.setAdapter(giftsRecyclerViewAdapter);

        optionsDialog.contentView(dialogView)
                .heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                .inDuration(500)
                .cancelable(true)
                .show();

        isOpened = false;

    }
}
