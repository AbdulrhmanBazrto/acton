package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.MessagesConversationActivity;
import com.gnusl.wow.Activities.SearchActivity;
import com.gnusl.wow.Adapters.MessageRecyclerViewAdapter;
import com.gnusl.wow.Delegates.MessageSectionDelegate;
import com.gnusl.wow.Enums.FragmentTags;
import com.gnusl.wow.Models.MessageSection;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class MessagesContainerFragment extends Fragment implements MessageSectionDelegate {

    private View inflatedView;
    private Fragment mCurrentFragment;

    public MessagesContainerFragment() {
    }

    public static MessagesContainerFragment newInstance() {

        return new MessagesContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_container_messages, container, false);

        // user info
        setUserInformation();

        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v -> ((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START));

        // open new friend
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.user_icon).setOnClickListener(v -> goToSearchActivity());

        // default
        replaceFragment(FragmentTags.MessagesFragment);

        return inflatedView;
    }

    private void setUserInformation() {

        User user = SharedPreferencesUtils.getUser();
        if (user != null) {

            // user image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                Glide.with(this)
                        .load(user.getImage_url())
                        .into(((ImageView) inflatedView.findViewById(R.id.right_icon)));
        }
    }

    public void replaceFragment(FragmentTags fragmentTags) {

        // init manager
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (fragmentTags) {

            case MessagesFragment:

                mCurrentFragment = MessagesFragment.newInstance(this);
                transaction.replace(R.id.frame_container, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case UsersMessagesFragment:

                mCurrentFragment = UsersMessagesFragment.newInstance();
                transaction.replace(R.id.frame_container, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case SystemMessagesFragment:

                mCurrentFragment = SystemMessagesFragment.newInstance();
                transaction.replace(R.id.frame_container, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;
        }
    }

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    private void goToSearchActivity() {

        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra(SearchActivity.SEARCH_FOR_USERS,"");
        getActivity().startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

    @Override
    public void onClickFriendsSection() {

        replaceFragment(FragmentTags.UsersMessagesFragment);
    }

    @Override
    public void onClickSystemMessagesSection() {

        replaceFragment(FragmentTags.SystemMessagesFragment);
    }
}
