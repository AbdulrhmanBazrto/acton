package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Activities.ProfileActivity;
import com.gnusl.wow.Activities.ProfileBadgesActivity;
import com.gnusl.wow.Activities.ProfileGiftsActivity;
import com.gnusl.wow.Activities.ProfileMomentsActivity;
import com.gnusl.wow.Activities.ProfileRoomsActivity;
import com.gnusl.wow.R;

public class PathProfileFragment extends Fragment {

    private View inflatedView;

    public PathProfileFragment() {
    }

    public static PathProfileFragment newInstance() {

        return new PathProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_path_profile, container, false);

        findViews(inflatedView);

        return inflatedView;
    }

    private void findViews(View inflatedView){

        // go to badges
        inflatedView.findViewById(R.id.badge_section).setOnClickListener(v->{

            ProfileBadgesActivity.launch(getActivity(),((ProfileActivity)getActivity()).getUserId());
        });

        // gifts
        inflatedView.findViewById(R.id.gifts_section).setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), ProfileGiftsActivity.class));
        });

        // rooms
        inflatedView.findViewById(R.id.rooms_section).setOnClickListener(v -> {

            ProfileRoomsActivity.launch(getActivity(),((ProfileActivity)getActivity()).getUserId());
        });

        // moments
        inflatedView.findViewById(R.id.moments_section).setOnClickListener(v -> {

            ProfileMomentsActivity.launch(getActivity(),((ProfileActivity)getActivity()).getUserId());
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }
}
