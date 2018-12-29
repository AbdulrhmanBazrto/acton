package com.gnusl.wow.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gnusl.wow.Fragments.MessagesConversationFragment;
import com.gnusl.wow.R;

public class MessagesConversationActivity extends AppCompatActivity {

    public static final String USER_ID="user_id";
    private MessagesConversationFragment messagesConversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_conversation);

        if(getIntent().hasExtra(USER_ID)) {

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            messagesConversationFragment = MessagesConversationFragment.newInstance(getIntent().getExtras().getInt(USER_ID));
            fragmentTransaction.replace(R.id.frame_messages_container, messagesConversationFragment).commit();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        messagesConversationFragment.onDestroy();
        finish();
    }
}

