package com.gnusl.wow.Adapters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.FollowersActivity;
import com.gnusl.wow.Activities.FollowingActivity;
import com.gnusl.wow.Activities.ProfileGiftsActivity;
import com.gnusl.wow.Activities.UserDetailsActivity;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // back
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // details listener
        findViewById(R.id.details_button).setOnClickListener(v -> {

            startActivity(new Intent(this, UserDetailsActivity.class));
        });

        // followers
        findViewById(R.id.followers).setOnClickListener(v -> {

            startActivity(new Intent(this, FollowersActivity.class));
        });

        // following
        findViewById(R.id.following).setOnClickListener(v -> {

            startActivity(new Intent(this, FollowingActivity.class));
        });

        // gifts
        findViewById(R.id.gifts).setOnClickListener(v -> {

            startActivity(new Intent(this, ProfileGiftsActivity.class));
        });

        // refresh user details
        setUserInfo(SharedPreferencesUtils.getUser());
    }

    private void setUserInfo(User user) {

        if (user == null)
            return;

        // load image
        if (user.getImage_url() != null && !user.getImage_url().isEmpty())
            Glide.with(this)
                    .load(user.getImage_url())
                    .into((ImageView) findViewById(R.id.user_image));

        // name
        ((TextView)findViewById(R.id.user_name)).setText(user.getName());

    }

}
