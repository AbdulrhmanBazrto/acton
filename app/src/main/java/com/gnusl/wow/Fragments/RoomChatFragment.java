package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Activities.RoomSettingsActivity;
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Enums.UserAttendanceType;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Gift;
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

import static android.widget.Toast.LENGTH_SHORT;


public class RoomChatFragment extends Fragment implements ConnectionDelegate {

    private RoomChatActivity activity;
    private View inflatedView;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private UsersChatRoomRecyclerViewAdapter usersChatRoomRecyclerViewAdapter;
    private UsersScoreRoomRecyclerViewAdapter usersScoreRoomRecyclerViewAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Gift> gifts;

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

        // get messages
        getAllMessagesRequest();

        // get gifts
        APIConnectionNetwork.GetGifts(this);
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
                GiftsRoomDialog.show(getContext(), gifts);
        });

        inflatedView.findViewById(R.id.gallery_btn).setOnClickListener(v -> {

            ((RoomChatActivity) getActivity()).openGallery(false);
        });

        inflatedView.findViewById(R.id.music_image).setOnClickListener(v -> {

            showUsersAttendancePopUp();
        });

    }

    private void goToRoomSettingsActivity() {

        Intent intent = new Intent(getActivity(), RoomSettingsActivity.class);
        intent.putExtra(RoomSettingsActivity.CHANNEL_KEY, ((RoomChatActivity) getActivity()).getRoom());
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

        usersChatRoomRecyclerViewAdapter = new UsersChatRoomRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(usersChatRoomRecyclerViewAdapter);
    }

    private void initializeChatAdapter() {

        RecyclerView recyclerView = inflatedView.findViewById(R.id.chat_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(chatRecyclerViewAdapter);
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

                // get message
                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                System.out.println("msg content: " + msg);

                // get user name
                String userName = message.getMessage().getAsJsonObject().get("user_name").getAsString();

                // get user image
                String userImage = message.getMessage().getAsJsonObject().get("user_image").getAsString();

                // create model
                ChatMessage chatMessage = new ChatMessage(msg, userName, userImage);

                // show message
                activity.runOnUiThread(() -> {

                    chatRecyclerViewAdapter.getChatMessages().add(chatMessage);
                    chatRecyclerViewAdapter.notifyDataSetChanged();
                });

                // check if should generate crazy words
                if (message.getMessage().getAsJsonObject().has("crazy_word_for_user_id")) {

                    // check if for this user
                    if (message.getMessage().getAsJsonObject().get("crazy_word_for_user_id").getAsInt() == SharedPreferencesUtils.getUser().getId())
                        shouldGenerateCrazyWords = true;
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

    public static String generateRandomWords() {

        Random random = new Random();
        char[] word = new char[random.nextInt(6) + 4];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(12));
        }

        return word.toString();
    }

    private void getAllMessagesRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading messages..");

        // send request
        APIConnectionNetwork.GetAllRoomMessages(activity.getRoom().getId(), this);
    }

    private boolean shouldSendCrazyWordsToUser = false;
    private User randomWordUser = null;

    private void showUsersAttendancePopUp() {

        if (!usersChatRoomRecyclerViewAdapter.getUsers().isEmpty()) {

            Toast.makeText(getContext(), "there isn't any user in room", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TintTheme);

        HashMap<String, Integer> hashMap = new HashMap<>();

        for (User user : usersChatRoomRecyclerViewAdapter.getUsers())
            hashMap.put(user.getName(), user.getId());

        // String array for alert dialog multi choice items
        String[] teacher_strings = new String[usersChatRoomRecyclerViewAdapter.getUsers().size()];
        for (int i = 0; i < usersChatRoomRecyclerViewAdapter.getUsers().size(); i++)
            teacher_strings[i] = usersChatRoomRecyclerViewAdapter.getUsers().get(i).getName();

        // Set multiple choice items for alert dialog

        builder.setMultiChoiceItems(teacher_strings, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                randomWordUser = usersChatRoomRecyclerViewAdapter.getUsers().get(which);
                if (randomWordUser != null)
                    shouldSendCrazyWordsToUser = true;

            }
        });

        // Specify the dialog is not cancelable
        builder.setCancelable(true);

        // Set a title for alert dialog
        builder.setTitle("Users");

        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click positive button

            }
        });


        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
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

        } else if (jsonObject.has("users")) { // refresh users in room
            try {
                usersChatRoomRecyclerViewAdapter.setUsers(User.parseJSONArray(jsonObject.getJSONArray("users")));
                usersChatRoomRecyclerViewAdapter.notifyDataSetChanged();
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
        }

        // dismiss
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

        // parsing
        ArrayList<ChatMessage> chatMessages = ChatMessage.parseJSONArray(jsonArray);

        ArrayList<ChatMessage> messages = new ArrayList<>();
        for (int i = chatMessages.size() - 1; i >= 0; i--)
            messages.add(chatMessages.get(i));

        // notify
        chatRecyclerViewAdapter.setChatMessages(messages);
        chatRecyclerViewAdapter.notifyDataSetChanged();

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
}


