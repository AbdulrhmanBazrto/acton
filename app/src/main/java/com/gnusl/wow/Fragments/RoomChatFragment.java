package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Activities.UsersInRoomActivity;
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MicUsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Delegates.GiftDelegate;
import com.gnusl.wow.Delegates.MicUserDelegate;
import com.gnusl.wow.Delegates.OnLoadMoreListener;
import com.gnusl.wow.Delegates.UserAttendanceDelegate;
import com.gnusl.wow.Enums.UserAttendanceType;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.MicUser;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Popups.GiftsRoomDialog;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.LENGTH_SHORT;


public class RoomChatFragment extends Fragment implements ConnectionDelegate, OnLoadMoreListener, GiftDelegate, MicUserDelegate {

    private static final int PAGE_SIZE_ITEMS = 5;

    private RoomChatActivity activity;
    private View inflatedView;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private RecyclerView chatRecyclerView;
    private MicUsersRecyclerViewAdapter micUsersRecyclerViewAdapter;
    private UsersScoreRoomRecyclerViewAdapter usersScoreRoomRecyclerViewAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Gift> gifts;
    private boolean isRefreshing;
    private boolean isRechToLimit = false;
    private boolean shouldSendCrazyWordsToUser = false;
    private User randomWordUser = null;

    // pubnub
    private PubNub pubnub;
    String channelName = "awesomeChannel";

    public RoomChatFragment() {
    }

    public static RoomChatFragment newInstance() {

        return new RoomChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflatedView = inflater.inflate(R.layout.fragment_roome_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize views
        initializeViews();

        // score user
        initializeUsersScore();

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
        getAllMessagesRequest();

        // get gifts
        APIConnectionNetwork.GetGifts(this);

        // animate entrance
        animateEntranceUser();

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

        inflatedView.findViewById(R.id.frame).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).onRequestToHideKeyboard();

            inflatedView.findViewById(R.id.frame).setVisibility(View.GONE);
        });

        inflatedView.findViewById(R.id.write_message_btn).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).onRequestToShowKeyboard();

            inflatedView.findViewById(R.id.frame).setVisibility(View.VISIBLE);
        });

        inflatedView.findViewById(R.id.more_icon).setOnClickListener(v -> {

            PopupMenu dropDownMenu = new PopupMenu(getContext(), inflatedView.findViewById(R.id.more_icon));
            dropDownMenu.getMenuInflater().inflate(R.menu.more_room_option, dropDownMenu.getMenu());
            dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {

                        case R.id.settings_action:

                            goToRoomSettingsActivity();
                            break;

                        case R.id.members:

                            // go to users in channel
                            goToUsersInRoomActivity();
                            break;

                        case R.id.backround_action:

                            ((RoomChatActivity) getActivity()).openGallery(true);
                            break;
                    }

                    return true;
                }
            });
            dropDownMenu.show();
        });


        inflatedView.findViewById(R.id.gift_image).setOnClickListener(v -> {

            if (gifts != null)
                GiftsRoomDialog.show(getContext(), gifts, this);
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
    }

    private void animateEntranceUser() {

        if (getContext() == null)
            return;

        // fill info
        ((TextView) inflatedView.findViewById(R.id.entrance_user_name)).setText(SharedPreferencesUtils.getUser().getName());
        inflatedView.findViewById(R.id.entrance_layout_animation).setVisibility(View.VISIBLE);

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

        RecyclerView recyclerView = inflatedView.findViewById(R.id.score_user_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        usersScoreRoomRecyclerViewAdapter = new UsersScoreRoomRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(usersScoreRoomRecyclerViewAdapter);
    }

    private void initializeUsersInRoomAdapter() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        micUsersRecyclerViewAdapter = new MicUsersRecyclerViewAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(micUsersRecyclerViewAdapter);
    }

    private void initializeChatAdapter() {

        chatRecyclerView = inflatedView.findViewById(R.id.chat_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext(), chatRecyclerView, new ArrayList<>(), this);
        chatRecyclerView.setAdapter(chatRecyclerViewAdapter);
    }

    private void setUserInformation() {

        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // name
            ((TextView) inflatedView.findViewById(R.id.user_name)).setText(user.getName());

            // Id
            ((TextView) inflatedView.findViewById(R.id.user_Id)).setText(String.valueOf("ID:" + user.getId()));

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(getContext())
                        .load(user.getImage_url())
                        .into(((ImageView) inflatedView.findViewById(R.id.user_image)));
        }
    }

    private boolean shouldGenerateCrazyWords = false;

    private void PubnubImplementation() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("demo");
        pnConfiguration.setPublishKey("demo");

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


                // check services messages
                if (message.getMessage().getAsJsonObject().has("Refresh_MIC_USERS")) {

                    // get mic users
                    APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), RoomChatFragment.this);

                } else {

                    // get user name
                    String userName = message.getMessage().getAsJsonObject().get("user_name").getAsString();

                    // get user image
                    String userImage = message.getMessage().getAsJsonObject().get("user_image").getAsString();

                    // create model
                    ChatMessage chatMessage;

                    // check message
                    if (message.getMessage().getAsJsonObject().has("msg")) {
                        String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                        System.out.println("msg content: " + msg);

                        chatMessage = new ChatMessage(msg, userName, userImage);

                    } else { // gift case

                        String gift_path = message.getMessage().getAsJsonObject().get("gift_image").getAsString();
                        String userGiftName = message.getMessage().getAsJsonObject().get("user_gift_name").getAsString();
                        chatMessage = new ChatMessage();
                        chatMessage.setGiftImagePath(gift_path);
                        chatMessage.setGiftUserName(userGiftName);
                        chatMessage.setUserName(userName);
                        chatMessage.setUserImage(userImage);

                    }

                    // show message
                    activity.runOnUiThread(() -> {

                        chatRecyclerViewAdapter.getChatMessages().add(chatMessage);
                        chatRecyclerViewAdapter.notifyDataSetChanged();

                        // smooth scroll
                        chatRecyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getChatMessages().size() - 1);

                        // gift animation
                        if (chatMessage.getGiftImagePath() != null)
                            animateGiftInfo(chatMessage);
                    });

                    // check if should generate crazy words
                    if (message.getMessage().getAsJsonObject().has("crazy_word_for_user_id")) {

                        // check if for this user
                        if (message.getMessage().getAsJsonObject().get("crazy_word_for_user_id").getAsInt() == SharedPreferencesUtils.getUser().getId())
                            shouldGenerateCrazyWords = true;
                    }
                }
            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList(channelName)).execute();

    }

    int generateCrazyWordsIndex = 0;

    public void ShareMessageOnPubnub(String message) {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        // check generate crazy words
        if (shouldGenerateCrazyWords) {
            generateCrazyWordsIndex++;
            messageJsonObject.addProperty("msg", generateRandomWords());
        } else
            messageJsonObject.addProperty("msg", message);

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
        messageJsonObject.addProperty("gift_image", gift.getPath());
        messageJsonObject.addProperty("user_gift_name", userGift.getName());

        // add user info
        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // name
            messageJsonObject.addProperty("user_name", user.getName());

            // image
            messageJsonObject.addProperty("user_image", user.getImage_url());
        }

        pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // Check whether request successfully completed or not.
            }
        });
    }

    public void SendToRefreshMicUsersOnPubnub() {

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();

        // add gift image
        messageJsonObject.addProperty("Refresh_MIC_USERS", "");

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
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading messages..");

        // send request
        APIConnectionNetwork.GetAllRoomMessages(activity.getRoom().getId(), PAGE_SIZE_ITEMS, chatRecyclerViewAdapter.getChatMessages().size(), this);

    }

    private void showUsersAttendancePopUp(UserAttendanceDelegate userAttendanceDelegate) {

        this.progressDialog = ProgressDialog.show(getContext(), "", "get active users..");

        // get user attendance
        APIConnectionNetwork.GetUserAttendance(activity.getRoom().getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionFailure() {

                Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

                if (progressDialog != null)
                    progressDialog.dismiss();

            }

            @Override
            public void onConnectionError(ANError anError) {

                Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

                if (progressDialog != null)
                    progressDialog.dismiss();

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

                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            @Override
            public void onConnectionSuccess(JSONArray jsonArray) {

            }
        });

    }


    @Override
    public void onConnectionFailure() {

        Toast.makeText(getContext(), " Connection Failure", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void onConnectionError(ANError anError) {

        Toast.makeText(getContext(), "Error Connection try again", LENGTH_SHORT).show();

        if (progressDialog != null)
            progressDialog.dismiss();

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
        } else if (jsonObject.has("score_users")) { // refresh scores in room
            try {
                usersScoreRoomRecyclerViewAdapter.setUsers(User.parseJSONArray(jsonObject.getJSONArray("score_users")));
                usersScoreRoomRecyclerViewAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (jsonObject.has("mic_status")) {

            // should refresh
            APIConnectionNetwork.GetMicUsers(activity.getRoom().getId(), this);

            // shold send to all users using pubnup to refresh

        }

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
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
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();

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
    public void onClickToSendGift(Gift gift) {

        // show users
        showUsersAttendancePopUp(user -> {

            // store gift
            APIConnectionNetwork.StoreGift(activity.getRoom().getId(), user.getId(), gift.getId(), null);

            // send on pubnup
            ShareGiftOnPubnub(user, gift);
        });
    }

    @Override
    public void onTakeMic(int micId) {

        // TODO: should show popup

        // permission
        activity.checkPermissions();

        // send request
        APIConnectionNetwork.SetMicForUser(activity.getRoom().getId(), micId, this);
    }

    @Override
    public void onSelectUserOnMic(MicUser micUser) {

    }

    public boolean isRechToLimit() {
        return isRechToLimit;
    }

    public void setRechToLimit(boolean rechToLimit) {
        isRechToLimit = rechToLimit;
    }

}


