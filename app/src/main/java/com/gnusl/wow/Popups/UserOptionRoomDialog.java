package com.gnusl.wow.Popups;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.wow.Adapters.UsersAristRecyclerViewAdapter;
import com.gnusl.wow.Delegates.UserRoomActionsDelegate;
import com.gnusl.wow.Enums.UserRoomActions;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.rey.material.app.BottomSheetDialog;
import com.squareup.picasso.Picasso;

public class UserOptionRoomDialog {

    public static boolean isOpened = false;

    public static void show(final Context context, User user, boolean isAdmin, boolean isAttendence, UserRoomActionsDelegate userRoomActionsDelegate) {
        if (isOpened) return;
        isOpened = true;
        View dialogView = LayoutInflater.from(context).inflate(R.layout.user_options_dialog_layout, null);

        TextView tvUsername = dialogView.findViewById(R.id.tv_username);
        ImageView ivUserImage = dialogView.findViewById(R.id.iv_user_image);
        ImageView ivKickOut = dialogView.findViewById(R.id.iv_kick_out);
        ImageView ivBlock = dialogView.findViewById(R.id.iv_block);
        ImageView ivTakeMic = dialogView.findViewById(R.id.iv_take_mic);
        ImageView ivGiveMic = dialogView.findViewById(R.id.iv_give_mic);
        ImageView tvMuteUnMute = dialogView.findViewById(R.id.tv_mute_unMute);
        ImageView tvGift = dialogView.findViewById(R.id.tv_gift);

        View clActions = dialogView.findViewById(R.id.cl_actions);

        RecyclerView rvArist = dialogView.findViewById(R.id.arist_rv);
        if (isAttendence) {
            rvArist.setVisibility(View.VISIBLE);
            clActions.setVisibility(View.GONE);
            tvMuteUnMute.setVisibility(View.GONE);
        } else {
            rvArist.setVisibility(View.GONE);
            if (isAdmin) {
                clActions.setVisibility(View.VISIBLE);
                tvMuteUnMute.setVisibility(View.VISIBLE);
            } else {
                clActions.setVisibility(View.GONE);
                tvMuteUnMute.setVisibility(View.GONE);
                rvArist.setVisibility(View.VISIBLE);
            }
        }


        if (user.getUserAristocracies().size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

            rvArist.setLayoutManager(linearLayoutManager);

            UsersAristRecyclerViewAdapter usersAristRecyclerViewAdapter = new UsersAristRecyclerViewAdapter(context, user.getUserAristocracies());

            rvArist.setAdapter(usersAristRecyclerViewAdapter);
        }

        tvUsername.setText(user.getName());
        Picasso.with(context).load(user.getImage_url()).into(ivUserImage);


        ivKickOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.KickOut, user);
            }
        });

        ivBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.Block, user);
            }
        });

        ivTakeMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.TakeMic, user);
            }
        });

        ivGiveMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.GiveMic, user);
            }
        });

        tvMuteUnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.Mute, user);
            }
        });

        tvGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRoomActionsDelegate.onActionClick(UserRoomActions.Gift, user);
            }
        });


        final BottomSheetDialog optionsDialog = new BottomSheetDialog(context, R.style.Material_App_BottomSheetDialog);

        optionsDialog.contentView(dialogView)
                .heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                .inDuration(500)
                .cancelable(true)
                .show();

        isOpened = false;

    }
}
