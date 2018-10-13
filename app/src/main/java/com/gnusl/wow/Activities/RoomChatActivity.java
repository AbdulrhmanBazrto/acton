package com.gnusl.wow.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gnusl.wow.Adapters.ChatRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersChatRoomRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
import com.gnusl.wow.Models.ChatMessage;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class RoomChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);

        // score user
        initializeUsersScore();

        // users in room
        initializeUsersInRoomAdapter();

        // chat adapter
        initializeChatAdapter();
    }

    private void initializeUsersScore(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.score_user_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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


        UsersScoreRoomRecyclerViewAdapter usersScoreRoomRecyclerViewAdapter= new UsersScoreRoomRecyclerViewAdapter(this, users);
        recyclerView.setAdapter(usersScoreRoomRecyclerViewAdapter);
    }

    private void initializeUsersInRoomAdapter(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(R.drawable.img1,"Dr. Ahmed"));
        users.add(new User(R.drawable.img2,"AB qahtany"));
        users.add(new User(R.drawable.img3,"AlAnood"));
        users.add(new User(R.drawable.img1,"Dr. Ahmed"));
        users.add(new User(R.drawable.img2,"AB qahtany"));
        users.add(new User(R.drawable.img3,"AlAnood"));


        UsersChatRoomRecyclerViewAdapter usersChatRoomRecyclerViewAdapter= new UsersChatRoomRecyclerViewAdapter(this, users);
        recyclerView.setAdapter(usersChatRoomRecyclerViewAdapter);
    }

    private void initializeChatAdapter(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<ChatMessage> chatMessages= new ArrayList<>();
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));
        chatMessages.add(new ChatMessage(R.drawable.img1,"الدوسري الأصيل",""));


        ChatRecyclerViewAdapter chatRecyclerViewAdapter= new ChatRecyclerViewAdapter(this, chatMessages);
        recyclerView.setAdapter(chatRecyclerViewAdapter);
    }

}
