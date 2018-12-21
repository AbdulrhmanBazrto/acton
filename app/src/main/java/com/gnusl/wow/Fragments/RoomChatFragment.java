package com.gnusl.wow.Fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gnusl.wow.Activities.RoomChatActivity;
import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.MessageRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.Message;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
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

import java.util.ArrayList;
import java.util.Arrays;


public class RoomChatFragment extends Fragment {

    private View inflatedView;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;

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

        // pubnub implementation
        PubnubImplementation();
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

    }

    private void initializeUsersScore() {

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.score_user_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));
        users.add(new User(R.drawable.img1));


        UsersScoreRoomRecyclerViewAdapter usersScoreRoomRecyclerViewAdapter = new UsersScoreRoomRecyclerViewAdapter(getContext(), users);
        recyclerView.setAdapter(usersScoreRoomRecyclerViewAdapter);
    }

    private void initializeUsersInRoomAdapter() {

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(R.drawable.img1, "Dr. Ahmed"));
        users.add(new User(R.drawable.img2, "AB qahtany"));
        users.add(new User(R.drawable.img3, "AlAnood"));
        users.add(new User(R.drawable.img1, "Dr. Ahmed"));
        users.add(new User(R.drawable.img2, "AB qahtany"));
        users.add(new User(R.drawable.img3, "AlAnood"));


        UsersChatRoomRecyclerViewAdapter usersChatRoomRecyclerViewAdapter = new UsersChatRoomRecyclerViewAdapter(getContext(), users);
        recyclerView.setAdapter(usersChatRoomRecyclerViewAdapter);
    }

    private void initializeChatAdapter() {

        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.chat_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(R.drawable.img1, "الدوسري الأصيل", "شباب ما هي أعراض القولون؟"));


        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext(), chatMessages);
        recyclerView.setAdapter(chatRecyclerViewAdapter);
    }

    private void PubnubImplementation() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("demo");
        pnConfiguration.setPublishKey("demo");

        PubNub pubnub = new PubNub(pnConfiguration);

        String channelName = "awesomeChannel";

        // create message payload using Gson
        JsonObject messageJsonObject = new JsonObject();
        messageJsonObject.addProperty("msg", "hello");

        System.out.println("Message to send: " + messageJsonObject.toString());

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

                                    // Message successfully published to specified channel.
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
                    // Message has been received on channel group stored in
                    // message.getChannel()
                } else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                JsonElement receivedMessageObject = message.getMessage();
                System.out.println("Received message content: " + receivedMessageObject.toString());
                // extract desired parts of the payload, using Gson
                String msg = message.getMessage().getAsJsonObject().get("msg").getAsString();
                System.out.println("msg content: " + msg);

                // show message
                getActivity().runOnUiThread(() -> {

                    chatRecyclerViewAdapter.getChatMessages().add(new ChatMessage(R.drawable.img1, "الدوسري الأصيل", msg));
                    chatRecyclerViewAdapter.notifyDataSetChanged();
                });

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

        // send when click on music
        inflatedView.findViewById(R.id.music_image).setOnClickListener(v -> {

            //SendMessagePopup.show(this);
            pubnub.publish().channel(channelName).message(messageJsonObject).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    // Check whether request successfully completed or not.
                    if (!status.isError()) {

                        // Message successfully published to specified channel.
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

        });

    }

}


