package com.gnusl.wow.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gnusl.wow.Adapters.UsersRecyclerViewAdapter;
import com.gnusl.wow.Adapters.UsersScoreRoomRecyclerViewAdapter;
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
}
