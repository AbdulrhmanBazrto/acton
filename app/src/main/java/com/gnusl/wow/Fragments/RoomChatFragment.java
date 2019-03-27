package com.gnusl.wow.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.DevelopRoomActivity;
import com.gnusl.wow.Activities.RechargeActivity;
import com.gnusl.wow.Activities.RoomBackgroundActivity;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Activities.RoomLockSettingActivity;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Activities.RoomTopGiftsActivity;
import com.gnusl.wow.Activities.UsersInRoomActivity;
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MicUsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ChooseUserDelegate;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Delegates.MicUserDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Delegates.SendGiftClickDelegate;
import com.gnusl.wow.Delegates.ShowHeartsClickListner;
import com.gnusl.wow.Delegates.UserAttendanceDelegate;
import com.gnusl.wow.Delegates.UserAttendenceDelegate;
import com.gnusl.wow.Delegates.UserRoomActionsDelegate;
import com.gnusl.wow.Enums.UserAttendanceType;
import com.gnusl.wow.Enums.UserRoomActions;
import com.gnusl.wow.Models.Aristocracy;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.MicUser;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.GiftsRoomDialog;
import com.gnusl.wow.Popups.LoaderPopUp;
import com.gnusl.wow.Popups.UserOptionRoomDialog;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
import com.gnusl.wow.Utils.ZigZagAnimation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;


public class RoomChatFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener, GiftDelegate,
        MicUserDelegate, ShowHeartsClickListner, SendGiftClickDelegate,
        ChooseUserDelegate, UserAttendenceDelegate {

    private static final int PAGE_SIZE_ITEMS = 5;
    private Room room;

    private RoomChatActivity activity;
    private View inflatedView, cl_room_name;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private RecyclerView chatRecyclerView;
    private MicUsersRecyclerViewAdapter micUsersRecyclerViewAdapter;
    private UsersScoreRoomRecyclerViewAdapter usersScoreRoomRecyclerViewAdapter;
    private ArrayList<Gift> gifts;
    private boolean isRefreshing;
    private boolean isRechToLimit = false;
    private boolean shouldSendCrazyWordsToUser = false;
    private User randomWordUser = null;

    // pubnub
    private PubNub pubnub;
    String channelName = "awesomeChannel";


    Button btnRoomFollow;
    private Gift DesigerGift;
    private User DesigerUser;

    public RoomChatFragment() {
    }

    @SuppressLint("ValidFragment")
    public RoomChatFragment(Room room) {
        this.room = room;
    }

    public static RoomChatFragment newInstance(Room room) {

        return new RoomChatFragment(room);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflatedView = inflater.inflate(R.layout.fragment_roome_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        channelName = "Channel_" + activity.getRoom().getId();

        // initialize views
        initializeViews();

        // score user
//        initializeUsersScore();
        setUpUserAttendanceRecycler();

        // users in room
        initializeUsersInRoomAdapter();

        // chat adapter
        initializeChatAdapter();

        // user info
        setUserInformation();

        // pubnub implementation
        PubnubImplementation();

        // set attendance
        APIConnectionNetwork.SetUserAttendance(UserAttendanceType.Entrance, activity.getRoom().getId(), this);

        // get scores
        APIConnectionNetwork.GetScoreUsers(activity.getRoom().getId(), this);

        // get mic users
        APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), this);

        // get messages
//        getAllMessagesRequest();

        // get gifts
        APIConnectionNetwork.GetGifts(this);

        // animate entrance
//        animateEntranceUser(SharedPreferencesUtils.getUser());
        shareEnterenceOnPubNub();

        // animate news
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (getActivity() != null)
                    getActivity().runOnUiThread(() -> animateNewsLayout());
            }
        }, 2000, 10000);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RoomChatActivity)
            activity = (RoomChatActivity) context;
    }

    private void initializeViews() {
        btnRoomFollow = inflatedView.findViewById(R.id.btn_follow);
        inflatedView.findViewById(R.id.frame).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).onRequestToHideKeyboard();

            inflatedView.findViewById(R.id.frame).setVisibility(View.GONE);
        });

        inflatedView.findViewById(R.id.write_message_btn).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).onRequestToShowKeyboard();

            inflatedView.findViewById(R.id.frame).setVisibility(View.VISIBLE);
        });

        inflatedView.findViewById(R.id.cl_room_name).setOnClickListener(v -> {
            showRoomNameDialog();
        });

        inflatedView.findViewById(R.id.more_icon).setOnClickListener(v -> {

            if (activity.getRoom().getUserId() == SharedPreferencesUtils.getUser().getId()) {
                PopupMenu dropDownMenu = new PopupMenu(getContext(), inflatedView.findViewById(R.id.more_icon));
                dropDownMenu.getMenuInflater().inflate(R.menu.more_room_option, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.action_settings:
                                goToRoomSettingsActivity();
                                break;

                            case R.id.action_members:

                                // go to users in channel
                                goToUsersInRoomActivity();
                                break;

                            case R.id.action_themes:
                                goToRoomThemesActivity();
                                break;
                            case R.id.action_develop:
                                goToRoomDevelopActivity();
                                break;
                            case R.id.action_lock:
                                goToRoomSettingsLockActivity();
                                break;
                            case R.id.action_mute:

                                break;
                        }

                        return true;
                    }
                });
                dropDownMenu.show();
            } else {
                PopupMenu dropDownMenu = new PopupMenu(getContext(), inflatedView.findViewById(R.id.more_icon));
                dropDownMenu.getMenuInflater().inflate(R.menu.more_room_option_user, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.action_members:

                                // go to users in channel
                                goToUsersInRoomActivity();
                                break;

                            case R.id.action_mute:

                                break;
                        }

                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });


        inflatedView.findViewById(R.id.gift_image).setOnClickListener(v -> {

            showGiftsDialog(null);

        });

        inflatedView.findViewById(R.id.gallery_btn).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).openGallery(false);
        });

        inflatedView.findViewById(R.id.crazy_words_image).setOnClickListener(v -> {

            showUsersAttendancePopUp(user -> {

                randomWordUser = user;
                if (randomWordUser != null)
                    shouldSendCrazyWordsToUser = true;
            });

        });

        inflatedView.findViewById(R.id.save_image).setOnClickListener(v -> {

            activity.setShouldLogout(false);
            inflatedView.findViewById(R.id.logout_frame).setVisibility(View.GONE);

        });

        inflatedView.findViewById(R.id.logout_image).setOnClickListener(v -> {

            activity.setShouldLogout(true);
            activity.onBackPressed();

            shareExitOnPubNub();

        });

        inflatedView.findViewById(R.id.logout_frame).setOnClickListener(v -> {

            activity.setShouldLogout(false);
            inflatedView.findViewById(R.id.logout_frame).setVisibility(View.GONE);

        });

        inflatedView.findViewById(R.id.exit_icon).setOnClickListener(v -> {

            showLogOutFrame();

        });

        // headset vs speaker mode
        inflatedView.findViewById(R.id.headset_or_speaker).setOnClickListener(v -> {

            if (!activity.isSpeakerPhoneMode()) {

                activity.setSpeakerMode();
                ((ImageView) inflatedView.findViewById(R.id.headset_or_speaker)).setImageResource(R.drawable.speaker);
            } else {

                activity.setHeadSetMode();
                ((ImageView) inflatedView.findViewById(R.id.headset_or_speaker)).setImageResource(R.drawable.headset);
            }

        });

        inflatedView.findViewById(R.id.share_button).setOnClickListener(v -> {

            shareChannel();
        });

        inflatedView.findViewById(R.id.follow_score).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RoomTopGiftsActivity.class);
            intent.putExtra("roomId", room.getId());
            startActivity(intent);
        });

        inflatedView.findViewById(R.id.iv_follow_room).setOnClickListener(v -> {
            followUnfollowRoom();
        });
    }

    private void showGiftsDialog(User user) {
        APIConnectionNetwork.GetUserAttendance(activity.getRoom().getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

                Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionError(ANError anError) {

                Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

                if (jsonObject.has("users")) { // refresh scores in room

                    try {
                        ArrayList<User> users = User.parseJSONArray(jsonObject.getJSONArray("users"));
                        if (gifts != null) {
                            if (users.size() == 0) {
                                Toast.makeText(getActivity(), getString(R.string.no_users_to_send_to), LENGTH_SHORT).show();
                            } else
                                GiftsRoomDialog.show(getContext(), gifts, users, RoomChatFragment.this, RoomChatFragment.this, RoomChatFragment.this, user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });

    }

    private void showRoomNameDialog() {
        if (getActivity() != null) {

            View viewById = inflatedView.findViewById(R.id.cl_room_info);
            viewById.setVisibility(View.VISIBLE);

            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewById.setVisibility(View.GONE);
                }
            });

            ImageView roomImage = inflatedView.findViewById(R.id.iv_room_image);
            TextView tvRoomLevel = inflatedView.findViewById(R.id.tv_room_level);
            TextView tvRoomName = inflatedView.findViewById(R.id.tv_room_name);
            TextView tvRoomId = inflatedView.findViewById(R.id.tv_room_id);
            TextView tvLanguage = inflatedView.findViewById(R.id.tv_language);
            TextView tvRoomMembersCount = inflatedView.findViewById(R.id.tv_room_members_count);
            ImageView ivRoomLocation = inflatedView.findViewById(R.id.iv_room_location);
            Button btnRoomJoin = inflatedView.findViewById(R.id.btn_join);

            if (room.isFollowing()) {
                btnRoomFollow.setText("following");
                btnRoomFollow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_full_heart), null, null, null);
                ((ImageView) inflatedView.findViewById(R.id.iv_follow_room)).setImageResource(R.drawable.icon_full_heart);
            } else {
                btnRoomFollow.setText("follow");
                btnRoomFollow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_empty_heart), null, null, null);
                ((ImageView) inflatedView.findViewById(R.id.iv_follow_room)).setImageResource(R.drawable.icon_empty_heart);
            }

            tvRoomName.setText(room.getName());

            btnRoomFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followUnfollowRoom();
                }
            });


            btnRoomJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            tvRoomId.setText(String.format("%s %d", getString(R.string.id_view), room.getId()));

            Glide.with(getActivity())
                    .load(room.getCountryCodeUrl())
                    .into(ivRoomLocation);

            tvRoomMembersCount.setText(String.format("%s %d", getString(R.string.users_view), room.getNumUsers()));

            APIConnectionNetwork.GetRoomInfo(room.getId(), new ConnectionDelegate() {
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
                    if (jsonObject.has("thumbnail_url"))
                        Glide.with(getActivity())
                                .load(jsonObject.optString("thumbnail_url"))
                                .into(roomImage);

                    tvRoomLevel.setText(getString(R.string.room_level) + jsonObject.optString("level"));
                    if (jsonObject.optString("language").equalsIgnoreCase("en"))
                        tvLanguage.setText("english");
                    else if (jsonObject.optString("language").equalsIgnoreCase("ar"))
                        tvLanguage.setText("العربية");

                }

                @Override
                public void onConnectionSuccess(JSONArray jsonArray) {

                }
            });


        }
    }

    private void followUnfollowRoom() {
        APIConnectionNetwork.FollowRoom(room.getId(), new ConnectionDelegate() {
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
                if (jsonObject.has("status")) {
                    if (jsonObject.optString("status").equalsIgnoreCase("follow")) {
                        btnRoomFollow.setText(getString(R.string.following_));
                        btnRoomFollow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_full_heart), null, null, null);
                        ((ImageView) inflatedView.findViewById(R.id.iv_follow_room)).setImageResource(R.drawable.icon_full_heart);
                    } else if (jsonObject.optString("status").equalsIgnoreCase("unfollow")) {
                        btnRoomFollow.setText(getString(R.string.follow__));
                        btnRoomFollow.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_empty_heart), null, null, null);
                        ((ImageView) inflatedView.findViewById(R.id.iv_follow_room)).setImageResource(R.drawable.icon_empty_heart);
                    }
                }
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }

    private void animateEntranceUser(User user) {

        if (getContext() == null)
            return;

        // fill info
        ((TextView) inflatedView.findViewById(R.id.entrance_user_name)).setText(user.getName());
        inflatedView.findViewById(R.id.entrance_layout_animation).setVisibility(View.VISIBLE);
        ImageView viewById = inflatedView.findViewById(R.id.arist_image);
        if (user.getUserAristocracies().size() > 0) {
            viewById.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(user.getUserAristocracies().get(0).getImageUrl()).into(viewById);
        } else {
            viewById.setVisibility(View.GONE);
        }

        new Handler().postDelayed(() -> {

            if (getContext() == null)
                return;

            inflatedView.findViewById(R.id.entrance_layout_animation).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_entrance_animation));
            inflatedView.findViewById(R.id.entrance_layout_animation).getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    inflatedView.findViewById(R.id.entrance_layout_animation).setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }, 2000);

//        shareEnterenceOnPubNub();
    }

    private void animateNewsLayout() {

        if (getContext() == null)
            return;

        // visible
        inflatedView.findViewById(R.id.news_layout_animation).setVisibility(View.VISIBLE);

        inflatedView.findViewById(R.id.news_layout_animation).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.news_bar_animation));
        inflatedView.findViewById(R.id.news_layout_animation).getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                inflatedView.findViewById(R.id.news_layout_animation).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


    private void shareChannel() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "تعال الى اكتون واعثر علي معرف الغرفة هو !" + String.valueOf(activity.getRoom().getId()) + " " + String.valueOf("http://acton.live"));
        getContext().startActivity(Intent.createChooser(sharingIntent, "مشاركة عن طريق"));

    }

    private void goToRoomSettingsLockActivity() {

        Intent intent = new Intent(getActivity(), RoomLockSettingActivity.class);
        intent.putExtra(RoomSettingsActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
        startActivity(intent);
    }

    private void goToRoomThemesActivity() {

        Intent intent = new Intent(getActivity(), RoomBackgroundActivity.class);
        intent.putExtra(RoomSettingsActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
        startActivity(intent);
    }

    private void goToRoomDevelopActivity() {

        Intent intent = new Intent(getActivity(), DevelopRoomActivity.class);
        intent.putExtra(RoomSettingsActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
        startActivity(intent);
    }

    private void animateGiftInfo(ChatMessage chatMessage) {

        if (getContext() == null)
            return;

        // fill info
        ((TextView) inflatedView.findViewById(R.id.user_gift_name)).setText(chatMessage.getUserName() != null ? chatMessage.getUserName() : "");
        ((TextView) inflatedView.findViewById(R.id.user_to_gift_name)).setText(chatMessage.getGiftUserName() != null ? String.valueOf(chatMessage.getGiftUserName() + "<<") : "");


        // user image
        if (chatMessage.getUserImage() != null && !chatMessage.getUserImage().isEmpty())
            Glide.with(getContext())
                    .load(chatMessage.getUserImage())
                    .into(((ImageView) inflatedView.findViewById(R.id.user_gift_image)));

        // gift image message
        if (chatMessage.getGiftImagePath() != null && !chatMessage.getGiftImagePath().isEmpty())
            Glide.with(getContext())
                    .load(chatMessage.getGiftImagePath())
                    .into(((ImageView) inflatedView.findViewById(R.id.gift_image_from_user)));


        inflatedView.findViewById(R.id.gift_layout_animation).setVisibility(View.VISIBLE);
        inflatedView.findViewById(R.id.gift_layout_animation).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.left_to_middle_gift_animation));

        inflatedView.findViewById(R.id.gift_layout_animation).getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(() -> {

                    if (getContext() == null)
                        return;

                    inflatedView.findViewById(R.id.gift_layout_animation).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.middle_to_right_gift_animation));
                }, 1600);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showLogOutFrame() {

        inflatedView.findViewById(R.id.logout_frame).setVisibility(View.VISIBLE);
    }

    private void goToRoomSettingsActivity() {

        Intent intent = new Intent(getActivity(), RoomSettingsActivity.class);
        intent.putExtra(RoomSettingsActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
        startActivity(intent);
    }

    private void goToUsersInRoomActivity() {

        Intent intent = new Intent(getActivity(), UsersInRoomActivity.class);
        intent.putExtra(UsersInRoomActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
        startActivity(intent);
    }

    private void initializeUsersScore() {


    }

    private void initializeUsersInRoomAdapter() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.users_recycler_view);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(linearLayoutManager);

        micUsersRecyclerViewAdapter = new MicUsersRecyclerViewAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(micUsersRecyclerViewAdapter);
    }

    private void initializeChatAdapter() {

        chatRecyclerView = inflatedView.findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    show();
                }
                return false;
            }
        });

        inflatedView.findViewById(R.id.root_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext(), chatRecyclerView, new ArrayList<>(), this, this);
        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);
    }

    private void setUserInformation() {

//        User user = SharedPreferencesUtils.getUser();
//        if (user != null) {

        // name
        if (activity.getRoom() != null) {
            ((TextView) inflatedView.findViewById(R.id.user_name)).setText(activity.getRoom().getName());

            // Id
            ((TextView) inflatedView.findViewById(R.id.user_Id)).setText(String.valueOf("ID:" + activity.getRoom().getId()));

            // user image
            if (activity.getRoom().getBackgroundUrl() != null && !activity.getRoom().getBackgroundUrl().isEmpty() && getActivity() != null)
                Glide.with(getActivity())
                        .load(activity.getRoom().getThumbnailUrl())
                        .into(((ImageView) inflatedView.findViewById(R.id.user_image)));

        }

    }

    private boolean shouldGenerateCrazyWords = false;

    // todo change gift animation as type sent
    private void PubnubImplementation() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-aa20a392-451f-11e9-8dbe-225b5c64e997");
        pnConfiguration.setPublishKey("pub-c-a723de90-86f2-4612-b578-af68a7037dba");

        this.pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                       /* pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // MessageSection successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });*/
                    }
                } else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                } else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // MessageSection has been received on channel group stored in
                    // message.getChannel()
                } else {
                    // MessageSection has been received on channel stored in
                    // message.getSubscription()
                }

                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Received message content: " + receivedMessageObject.toString());
                // extract desired parts of the payload, using Gson

                switch (message.getMessage().getAsJsonObject().get("EventType").getAsString()) {
                    case "MIC_MUTE_EVENT": {
                        if (message.getMessage().getAsJsonObject().get("USER_ID").getAsInt() == SharedPreferencesUtils.getUser().getId()) {
                            if (getActivity() instanceof RoomChatActivity)
                                ((RoomChatActivity) getActivity()).muteUnMuteMic();
                        }

                        break;
                    }

                    case "KICK_OUT_EVENT": {
                        if (message.getMessage().getAsJsonObject().get("USER_ID").getAsInt() == SharedPreferencesUtils.getUser().getId()) {
                            activity.setShouldLogout(true);
                            activity.onBackPressed();
                        }

                        break;
                    }
                    case "MIC_TAKE_EVENT": {
                        if (message.getMessage().getAsJsonObject().get("USER_ID").getAsInt() == SharedPreferencesUtils.getUser().getId()) {
                            boolean imOnMic = false;
                            for (MicUser mu : micUsersRecyclerViewAdapter.getMicUsers()) {
                                if (mu.getUser() != null)
                                    if (mu.getUser().getId() == SharedPreferencesUtils.getUser().getId())
                                        imOnMic = true;
                            }
                            boolean finalImOnMic = imOnMic;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!finalImOnMic) {
                                        AlertDialog alert = null;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        AlertDialog finalAlert = alert;
                                        builder.setMessage(R.string.invite_to_mic)
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), message.getMessage().getAsJsonObject().get("MIC_ID").getAsInt(), false, RoomChatFragment.this);
                                                        finalAlert.hide();
                                                    }
                                                })
                                                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finalAlert.hide();
                                                    }
                                                });
                                        alert = builder.create();
                                        alert.show();
                                    } else {
                                        APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), message.getMessage().getAsJsonObject().get("MIC_ID").getAsInt(), false, RoomChatFragment.this);
                                    }
                                }
                            });
                        }

                        break;
                    }

                    case "Refresh_MIC_USERS": {
                        // get mic users
                        APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), RoomChatFragment.this);

                        break;
                    }

                    case "ENTER_ROOM": {

                        if (message.getMessage().getAsJsonObject().get("USER_ID").getAsInt() == SharedPreferencesUtils.getUser().getId())
                            return;
                        User userUser = new User();
                        userUser.setId(message.getMessage().getAsJsonObject().get("USER_ID").getAsInt());
                        userUser.setName(message.getMessage().getAsJsonObject().get("user_name").getAsString());
                        userUser.setImage_url(message.getMessage().getAsJsonObject().get("user_image").getAsString());
                        userUser.setLevel(message.getMessage().getAsJsonObject().get("user_level").getAsInt());
                        if (message.getMessage().getAsJsonObject().has("user_aristocraty_image")) {
                            List<Aristocracy> aristocracies = new ArrayList<>();
                            Aristocracy aristocracy = new Aristocracy();
                            aristocracy.setImageUrl(message.getMessage().getAsJsonObject().get("user_aristocraty_image").getAsString());
                            aristocracies.add(aristocracy);
                            userUser.setUserAristocracies(aristocracies);
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                animateEntranceUser(userUser);
                            }
                        });


                        break;
                    }
                    case "OUT_ROOM": {

                        if (message.getMessage().getAsJsonObject().get("USER_ID").getAsInt() == SharedPreferencesUtils.getUser().getId())
                            return;
                        APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), RoomChatFragment.this);


                        break;
                    }

                    case "MESSAGE": {

                        // get user name
                        String userName = message.getMessage().getAsJsonObject().get("user_name").getAsString();

                        // get user image
                        String userImage = message.getMessage().getAsJsonObject().get("user_image").getAsString();

                        // create model
                        ChatMessage chatMessage;

                        // check message
                        String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();

                        chatMessage = new ChatMessage(msg, userName, userImage);

                        activity.runOnUiThread(() -> {
                            chatRecyclerViewAdapter.getChatMessages().add(chatMessage);
                            chatRecyclerViewAdapter.notifyDataSetChanged();

                            // smooth scroll
                            chatRecyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getChatMessages().size() - 1);
                        });

                        break;
                    }

                    case "GIFT": {

                        String userName = message.getMessage().getAsJsonObject().get("user_name").getAsString();
                        String userImage = message.getMessage().getAsJsonObject().get("user_image").getAsString();
                        String gift_path = message.getMessage().getAsJsonObject().get("gift_image").getAsString();
                        String gift_type = message.getMessage().getAsJsonObject().get("gift_type").getAsString();
                        String userGiftName = message.getMessage().getAsJsonObject().get("user_gift_name").getAsString();
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setGiftImagePath(gift_path);
                        chatMessage.setGiftUserName(userGiftName);
                        chatMessage.setGiftType(gift_type);
                        chatMessage.setUserName(userName);
                        chatMessage.setUserImage(userImage);

                        activity.runOnUiThread(() -> {
                            chatRecyclerViewAdapter.getChatMessages().add(chatMessage);
                            chatRecyclerViewAdapter.notifyDataSetChanged();
                            // smooth scroll
                            chatRecyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getChatMessages().size() - 1);
                        });

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                // gift animation
                                if (chatMessage.getGiftImagePath() != null) {
                                    switch (chatMessage.getGiftType()) {
                                        case "small":
                                        case "medium": {
                                            animateGiftInfo(chatMessage);
                                            break;
                                        }
                                        case "large": {
                                            ImageView imageBigGift = inflatedView.findViewById(R.id.iv_big_gift_animate);
                                            Picasso.with(getActivity()).load(chatMessage.getGiftImagePath()).into(imageBigGift, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    imageBigGift.setVisibility(View.VISIBLE);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            imageBigGift.setVisibility(View.GONE);
                                                        }
                                                    }, 1500);
                                                }

                                                @Override
                                                public void onError() {

                                                }
                                            });
                                            break;
                                        }
                                    }

                                }
                            }
                        });
                    }
                    break;
                }

                // check if should generate crazy words
                if (message.getMessage().getAsJsonObject().has("crazy_word_for_user_id")) {

                    // check if for this user
                    if (message.getMessage().getAsJsonObject().get("crazy_word_for_user_id").getAsInt() == SharedPreferencesUtils.getUser().getId())
                        shouldGenerateCrazyWords = true;
                }


            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
        List<String> channels = new ArrayList<>();
        channels.add(channelName);
        channels.add("actonGiftChannel");
        pubnub.subscribe().channels(channels).execute();

    }

    int generateCrazyWordsIndex = 0;

    public void ShareMessageOnPubnub(String message) {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        // check generate crazy words
        if (shouldGenerateCrazyWords) {
            generateCrazyWordsIndex++;
            messageJsonObject.addProperty("msg", generateRandomWords());
        } else {

            messageJsonObject.addProperty("EventType", "MESSAGE");
            if (message.contains(".png") || message.contains(".jpg") || message.contains(".jpeg"))
                messageJsonObject.addProperty("msg_type", "image");
            else
                messageJsonObject.addProperty("msg_type", "text");

            messageJsonObject.addProperty("msg", message);
        }

        // add user info
        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // name
            messageJsonObject.addProperty("user_name", user.getName());

            // image
            messageJsonObject.addProperty("user_image", user.getImage_url());
        }

        // crazy words
        if (shouldSendCrazyWordsToUser && randomWordUser != null) {

            messageJsonObject.addProperty("crazy_word_for_user_id", randomWordUser.getId());

            randomWordUser = null;
            shouldSendCrazyWordsToUser = false;
        }

        // case generate crazy

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
                if (!status.isError()) {

                    // MessageSection successfully published to specified channel.
                }
                // Request processing failed.
                else {

                    // Handle message publish error. Check 'category' property to find out possible issue
                    // because of which request did fail.
                    //
                    // Request can be resent using: [status retry];
                }
            }
        });

        if (generateCrazyWordsIndex > 3) {
            shouldGenerateCrazyWords = false;
            generateCrazyWordsIndex = 0;
        }
    }

    public void ShareGiftOnPubnub(User userGift, Gift gift) {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("EventType", "GIFT");
        messageJsonObject.addProperty("gift_image", gift.getPath());
        messageJsonObject.addProperty("gift_type", gift.getType());
        messageJsonObject.addProperty("user_gift_name", userGift.getName());

        // add user info
        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // name
            messageJsonObject.addProperty("user_name", user.getName());

            // image
            messageJsonObject.addProperty("user_image", user.getImage_url());
        }
        switch (gift.getType()) {
            case "small": {
                pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // Check whether request successfully completed or not.
                    }
                });
                break;
            }
            case "medium":
            case "large": {
                pubnub.publish().channel("actonGiftChannel").message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        // Check whether request successfully completed or not.
                    }
                });
                break;
            }

        }
    }

    public void SendToRefreshMicUsersOnPubnub() {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("EventType", "Refresh_MIC_USERS");

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }

    public static String generateRandomWords() {

        Random random = new Random();
        char[] word = new char[random.nextInt(6) + 4];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(12));
        }

        return word.toString();
    }

    private void getAllMessagesRequest() {

        if (chatRecyclerViewAdapter == null)
            return;

        isRefreshing = true;

        // make progress dialog
        LoaderPopUp.show(getActivity());

        // send request
        APIConnectionNetwork.GetAllRoomMessages(activity.getRoom().getId(), PAGE_SIZE_ITEMS, chatRecyclerViewAdapter.getChatMessages().size(), this);

    }

    private void setUpUserAttendanceRecycler() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.score_user_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersScoreRoomRecyclerViewAdapter = new UsersScoreRoomRecyclerViewAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(usersScoreRoomRecyclerViewAdapter);


        APIConnectionNetwork.GetUserAttendance(activity.getRoom().getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

                Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionError(ANError anError) {

                Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

                if (jsonObject.has("users")) { // refresh scores in room

                    try {
                        ArrayList<User> users = User.parseJSONArray(jsonObject.getJSONArray("users"));
                        usersScoreRoomRecyclerViewAdapter.setUsers(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }

    private void showUsersAttendancePopUp(UserAttendanceDelegate userAttendanceDelegate) {

        LoaderPopUp.show(getActivity());

        // get user attendance
        APIConnectionNetwork.GetUserAttendance(activity.getRoom().getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

                Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionError(ANError anError) {

                Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

                LoaderPopUp.dismissLoader();

            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {

                if (jsonObject.has("users")) { // refresh scores in room
                    try {
                        ArrayList<User> users = User.parseJSONArray(jsonObject.getJSONArray("users"));

                        if (users.isEmpty()) {

                            Toast.makeText(getContext(), "there isn't any user in room", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // show popup
                        // Build an AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TintTheme);
                        HashMap<String, Integer> hashMap = new HashMap<>();

                        for (User user : users)
                            hashMap.put(user.getName(), user.getId());

                        // String array for alert dialog multi choice items
                        String[] users_strings = new String[users.size()];
                        for (int i = 0; i < users.size(); i++)
                            users_strings[i] = users.get(i).getName();

                        final User[] tempUser = {null};
                        // Set multiple choice items for alert dialog
                        builder.setMultiChoiceItems(users_strings, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                tempUser[0] = users.get(which);
                            }
                        });

                        // Specify the dialog is not cancelable
                        builder.setCancelable(true);

                        // Set a title for alert dialog
                        builder.setTitle("Users");

                        // Set the positive/yes button click listener
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // call delegate
                                if (userAttendanceDelegate != null && tempUser[0] != null)
                                    userAttendanceDelegate.onSelectUser(tempUser[0]);
                            }
                        });

                        AlertDialog dialog = builder.create();
                        // Display the alert dialog on interface
                        dialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LoaderPopUp.dismissLoader();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });

    }


    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        LoaderPopUp.dismissLoader();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(anError.getErrorBody());
            if (jsonObject.has("payment_status")) {
                if (jsonObject.optString("payment_status").equalsIgnoreCase("error")) {
                    startActivity(new Intent(getActivity(), RechargeActivity.class));
                }
            }
        } catch (JSONException e) {

        }

    }

    @Override
    public void onConnectionSuccess(String response) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        // parse gifts
        if (jsonObject.has("status")) {

            // get users in room
            APIConnectionNetwork.GetUserAttendance(activity.getRoom().getId(), this);

        } else if (jsonObject.has("gifts")) {
            try {
                this.gifts = Gift.parseJSONArray(jsonObject.getJSONArray("gifts"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (jsonObject.has("mics")) { // refresh users in room
            try {
                micUsersRecyclerViewAdapter.setMicUsers(MicUser.parseJSONArray(jsonObject.getJSONArray("mics")));
                micUsersRecyclerViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        else if (jsonObject.has("score_users")) { // refresh scores in room
//            try {
//                usersScoreRoomRecyclerViewAdapter.setUsers(User.parseJSONArray(jsonObject.getJSONArray("score_users")));
//                usersScoreRoomRecyclerViewAdapter.notifyDataSetChanged();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        else if (jsonObject.has("mic_status")) {

            // should refresh
            SendToRefreshMicUsersOnPubnub();
            APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), this);

            // shold send to all users using pubnup to refresh

        }

        // dismiss
        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {


        if (chatRecyclerViewAdapter != null) {
            // parsing
            ArrayList<ChatMessage> chatMessages = ChatMessage.parseJSONArray(jsonArray);

            ArrayList<ChatMessage> messages = new ArrayList<>();
            for (int i = chatMessages.size() - 1; i >= 0; i--)
                messages.add(chatMessages.get(i));

            // notify
            if (!chatRecyclerViewAdapter.getChatMessages().isEmpty()) {
                messages.addAll(chatRecyclerViewAdapter.getChatMessages());
                chatRecyclerViewAdapter.setChatMessages(messages);
                chatRecyclerViewAdapter.notifyDataSetChanged();

            } else { // first time

                chatRecyclerViewAdapter.setChatMessages(messages);
                chatRecyclerViewAdapter.notifyDataSetChanged();

                // first scroll
                chatRecyclerView.scrollToPosition(messages.size() - 1);

                // setup recycler listener
                new Handler().postDelayed(() -> {

                    chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (!chatRecyclerViewAdapter.isLoading() && !isRechToLimit && !recyclerView.canScrollVertically(-1)) {
                                Log.d("TOP ", true + "");

                                chatRecyclerViewAdapter.setLoading(true);
                                onLoadMore();

                            } else if (!chatRecyclerViewAdapter.isLoading() && !recyclerView.canScrollVertically(1)) {
                                Log.d("BOTTOM ", true + "");
                            }

                        }
                    });

                }, 5000);
            }

            // check limit
            if (chatMessages.isEmpty())
                isRechToLimit = true;

            // disable loading
            chatRecyclerViewAdapter.setLoading(false);
            isRefreshing = false;
        }

        // dismiss
        LoaderPopUp.dismissLoader();
    }

    @Override
    public void onStop() {
        super.onStop();

//        if (micUsersRecyclerViewAdapter.getCurrentUserMicId() != -1)
//            APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), micUsersRecyclerViewAdapter.getCurrentUserMicId(), null);
        // set attendance
        APIConnectionNetwork.SetUserAttendance(UserAttendanceType.LEAVE, activity.getRoom().getId(), this);

    }

    @Override
    public void onLoadMore() {

        if (!isRefreshing)
            // send request
            getAllMessagesRequest();
    }

    @Override
    public void onClickToSendGift(Gift gift, User toUser) {

        this.DesigerGift = gift;

    }

    AlertDialog alert1 = null;

    @Override
    public void onTakeMic(MicUser mic) {

        // permission
        activity.checkPermissions();

        // send request
        boolean imOnMic = false;
        if (activity.getRoom().getUserId() == SharedPreferencesUtils.getUser().getId()) {
            for (MicUser mu : micUsersRecyclerViewAdapter.getMicUsers()) {
                if (mu.getUser() != null)
                    if (mu.getUser().getId() == SharedPreferencesUtils.getUser().getId())
                        imOnMic = true;
            }

            if (!imOnMic) {
                if (!mic.getType().equalsIgnoreCase("locked")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    CharSequence[] languages = new CharSequence[2];
                    languages[0] = "take mic";
                    languages[1] = "lock mic";

                    builder.setItems(languages, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), mic.getMicId(), false, RoomChatFragment.this);
                                    break;
                                case 1:
                                    APIConnectionNetwork.SetMicForUserWithLock(activity.getRoom().getId(), mic.getMicId(), RoomChatFragment.this);
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    CharSequence[] languages = new CharSequence[1];
                    languages[0] = "unlock mic";

                    builder.setItems(languages, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // Arabic
                                    APIConnectionNetwork.SetMicForUserWithLock(activity.getRoom().getId(), mic.getMicId(), RoomChatFragment.this);
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
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                CharSequence[] languages = new CharSequence[1];
                if (!mic.getType().equalsIgnoreCase("locked"))
                    languages[0] = "lock mic";
                else
                    languages[0] = "unlock mic";


                builder.setItems(languages, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // English
                                APIConnectionNetwork.SetMicForUserWithLock(activity.getRoom().getId(), mic.getMicId(), RoomChatFragment.this);
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
        } else {
            if (mic.getType().equalsIgnoreCase("locked"))
                Toast.makeText(getActivity(), "mick is locked", LENGTH_SHORT).show();
            else {
                if (micUsersRecyclerViewAdapter.getCurrentUserMicId() == -1 || micUsersRecyclerViewAdapter.getCurrentUserMicId() == mic.getMicId()) {
                    if (activity.getRoom() != null && activity.getRoom().getSubscriptionPrice() > 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("سيتم خصم   " + activity.getRoom().getSubscriptionPrice() + "  ليرة ذهبية. موافق؟")
                                .setCancelable(false)
                                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), mic.getMicId(), true, RoomChatFragment.this);
                                        if (alert1 != null)
                                            alert1.hide();
                                    }
                                })
                                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alert1 != null)
                                            alert1.hide();
                                    }
                                });
                        alert1 = builder.create();
                        alert1.show();
                    } else {
                        APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), mic.getMicId(), false, RoomChatFragment.this);
                    }

                }
            }
        }

    }

    @Override
    public void onSelectUserOnMic(MicUser micUser) {
        if (activity.getRoom().getUserId() == SharedPreferencesUtils.getUser().getId() && micUser.getUser().getId() != SharedPreferencesUtils.getUser().getId()) {
            UserOptionRoomDialog.show(getActivity(), micUser.getUser(), true, false, new UserRoomActionsDelegate() {
                @Override
                public void onActionClick(UserRoomActions userRoomActions, User user) {
                    switch (userRoomActions) {
                        case Mute:
                            sendMuteUnMuteOnPubNub(user);
                            break;

                        case UnMute:
                            sendMuteUnMuteOnPubNub(user);
                            break;

                        case Block:

                            break;

                        case Gift:
                            showGiftsDialog(user);
                            break;
                        case GiveMic:
//                            sendTakeGiveMicOnPubNub(user, micUser.getMicId());
                            break;

                        case KickOut:
                            sendKickOutOnPubNub(user);
                            break;

                        case TakeMic:
                            sendTakeGiveMicOnPubNub(user, micUser.getMicId());
                            break;
                    }
                }
            });
        } else if (activity.getRoom().getUserId() == SharedPreferencesUtils.getUser().getId() && micUser.getUser().getId() == SharedPreferencesUtils.getUser().getId()) {
            APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), micUser.getMicId(), false, RoomChatFragment.this);
        } else if (micUsersRecyclerViewAdapter.getCurrentUserMicId() == micUser.getMicId()) {
            APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), micUser.getMicId(), false, RoomChatFragment.this);
        }
    }

    private void sendTakeGiveMicOnPubNub(User user, int mic_id) {
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("EventType", "MIC_TAKE_EVENT");
        messageJsonObject.addProperty("MIC_TAKE_VALUE", true);
        messageJsonObject.addProperty("USER_ID", user.getId());
        messageJsonObject.addProperty("MIC_ID", mic_id);

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }

    private void sendKickOutOnPubNub(User user) {
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("EventType", "KICK_OUT_EVENT");
        messageJsonObject.addProperty("USER_ID", user.getId());

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }


    private void shareEnterenceOnPubNub() {
        JsonObject messageJsonObject = new JsonObject();

        User currentUser = SharedPreferencesUtils.getUser();

        // add gift image
        messageJsonObject.addProperty("EventType", "ENTER_ROOM");
        messageJsonObject.addProperty("USER_ID", currentUser.getId());
        messageJsonObject.addProperty("user_name", currentUser.getId());
        messageJsonObject.addProperty("user_image", currentUser.getImage_url());
        messageJsonObject.addProperty("user_level", currentUser.getLevel());
        if (activity.getRoom().getUser().getUserAristocracies().size() > 0)
            messageJsonObject.addProperty("user_aristocraty_image", activity.getRoom().getUser().getUserAristocracies().get(0).getImageUrl());

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }

    private void shareExitOnPubNub() {
        JsonObject messageJsonObject = new JsonObject();

        User currentUser = SharedPreferencesUtils.getUser();

        // add gift image
        messageJsonObject.addProperty("EventType", "OUT_ROOM");
        messageJsonObject.addProperty("USER_ID", currentUser.getId());

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }

    private void sendMuteUnMuteOnPubNub(User user) {
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("EventType", "MIC_MUTE_EVENT");
        messageJsonObject.addProperty("MIC_MUTE_VALUE", true);
        messageJsonObject.addProperty("USER_ID", user.getId());

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });

    }

    public boolean isRechToLimit() {
        return isRechToLimit;
    }

    public void setRechToLimit(boolean rechToLimit) {
        isRechToLimit = rechToLimit;
    }

    @Override
    public void show() {
        ConstraintLayout rootView = inflatedView.findViewById(R.id.root_view);

        ImageView heartAnimation = new ImageView(getActivity());
        heartAnimation.setLayoutParams(new android.view.ViewGroup.LayoutParams(65, 65));
        heartAnimation.setId(View.generateViewId());
        Random rander = new Random();
        int Max = 4;
        int Min = 1;
        int i = rander.nextInt(Max - Min + 1);
        switch (i) {
            case 1: {
                heartAnimation.setImageResource(R.drawable.h4);
                break;
            }
            case 2: {
                heartAnimation.setImageResource(R.drawable.h2);
                break;
            }
            case 3: {
                heartAnimation.setImageResource(R.drawable.h3);
                break;
            }
            default:
                heartAnimation.setImageResource(R.drawable.h3);
                break;
        }

        rootView.addView(heartAnimation);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootView);

        constraintSet.connect(heartAnimation.getId(), ConstraintSet.BOTTOM, rootView.getId(), ConstraintSet.BOTTOM, 150);
        constraintSet.connect(heartAnimation.getId(), ConstraintSet.RIGHT, rootView.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(heartAnimation.getId(), ConstraintSet.LEFT, rootView.getId(), ConstraintSet.LEFT);


        constraintSet.applyTo(rootView);

//        heartAnimation.startAnimation(new ZigZagAnimation(activity));


        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(new ZigZagAnimation(activity));

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        animationSet.addAnimation(fadeOut);

        animationSet.start();

        heartAnimation.startAnimation(animationSet);

        heartAnimation.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation.getDuration() == 2000) {
                    heartAnimation.setVisibility(View.GONE);
                    rootView.removeView(heartAnimation);
                } else {
                    heartAnimation.setVisibility(View.GONE);
                    rootView.removeView(heartAnimation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpUserAttendanceRecycler();

        APIConnectionNetwork.GetRoomInfo(room.getId(), new ConnectionDelegate() {
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
                if (jsonObject.has("background_url") && getActivity() != null)
                    activity.changeBackGround(jsonObject.optString("background_url"));
                if (jsonObject.has("background_id") && getActivity() != null)
                    activity.getRoom().setBackgroundId(jsonObject.optInt("background_id"));

            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });


    }

    @Override
    public void sendClick() {
        if (DesigerGift == null) {
            Toast.makeText(getActivity(), "select gift", LENGTH_SHORT).show();
            return;
        }
        if (DesigerUser == null) {
            Toast.makeText(getActivity(), "select user", LENGTH_SHORT).show();
            return;
        }
        // store gift
        APIConnectionNetwork.StoreGift(activity.getRoom().getId(), DesigerUser.getId(), DesigerGift.getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

            }

            @Override
            public void onConnectionError(ANError anError) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(anError.getErrorBody());
                    if (jsonObject.has("payment_status")) {
                        if (jsonObject.optString("payment_status").equalsIgnoreCase("error")) {
                            startActivity(new Intent(getActivity(), RechargeActivity.class));
                        }
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onConnectionSuccess(String response) {

            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                // send on pubnup
                ShareGiftOnPubnub(DesigerUser, DesigerGift);
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });
    }

    @Override
    public void onSelectUser(User user) {
        this.DesigerUser = user;
    }

    @Override
    public void onUserClick(User user) {
        if (activity.getRoom().getUserId() == SharedPreferencesUtils.getUser().getId() && user.getId() != SharedPreferencesUtils.getUser().getId()) {
            UserOptionRoomDialog.show(getActivity(), user, true, true, new UserRoomActionsDelegate() {
                @Override
                public void onActionClick(UserRoomActions userRoomActions, User user) {
                    switch (userRoomActions) {
                        case Mute:
                            sendMuteUnMuteOnPubNub(user);
                            break;

                        case UnMute:
                            sendMuteUnMuteOnPubNub(user);
                            break;

                        case Block:

                            break;

                        case Gift:
                            showGiftsDialog(user);
                            break;
                        case GiveMic:
                            if (micUsersRecyclerViewAdapter.getFirstEmptyMicId() != 0) {
                                sendTakeGiveMicOnPubNub(user, micUsersRecyclerViewAdapter.getFirstEmptyMicId());
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.no_empty_mics), LENGTH_SHORT).show();
                            }
                            break;

                        case KickOut:
                            sendKickOutOnPubNub(user);
                            break;

                        case TakeMic:
                            if (micUsersRecyclerViewAdapter.getFirstEmptyMicId() != 0) {
                                sendTakeGiveMicOnPubNub(user, micUsersRecyclerViewAdapter.getFirstEmptyMicId());
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.no_empty_mics), LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
        } else if (activity.getRoom().getUserId() != SharedPreferencesUtils.getUser().getId() && user.getId() != SharedPreferencesUtils.getUser().getId()) {
            UserOptionRoomDialog.show(getActivity(), user, false, true, new UserRoomActionsDelegate() {
                @Override
                public void onActionClick(UserRoomActions userRoomActions, User user) {
                    switch (userRoomActions) {
                        case Gift:
                            showGiftsDialog(user);
                            break;
                    }
                }
            });
        }
    }
}


