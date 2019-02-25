package com.gnusl.wow.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Fragments.MyMomentsFragment;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

public class PrivateUserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_user_info);

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

        findViewById(R.id.ll_badges).setOnClickListener(v -> {
            startActivity(new Intent(this, BadgesActivity.class));
        });

        findViewById(R.id.visitors).setOnClickListener(v ->
                startActivity(new Intent(this, VisitorsActivity.class)));

        findViewById(R.id.moments).setOnClickListener(v ->
                startActivity(new Intent(this, MyMomentsActivity.class)));

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
        ((TextView) findViewById(R.id.user_name)).setText(user.getName());

    }

}
