package com.gnusl.wow.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.Adapters.ExploreRecyclerViewSectionAdapter;
import com.gnusl.wow.Adapters.RoomsRecyclerViewAdapter;
import com.gnusl.wow.Models.ActivitiesHashTag;
import com.gnusl.wow.Models.Country;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/27/2018.
 */

public class ExploreRoomsFragment extends Fragment {

    private View inflatedView;

    public ExploreRoomsFragment() {
    }

    public static ExploreRoomsFragment newInstance() {

        return new ExploreRoomsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_explore_rooms, container, false);


        RecyclerView recyclerView = (RecyclerView) inflatedView.findViewById(R.id.explore_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set Data
        ArrayList<ExploreSection> exploreSections=new ArrayList<>();


        ExploreSection exploreSection=new ExploreSection();
        ExploreSection exploreSection1=new ExploreSection();
        ExploreSection exploreSection2=new ExploreSection();
        ExploreSection exploreSection3=new ExploreSection();

        exploreSection.setHeaderTitle("Top");
        exploreSection1.setHeaderTitle("Countries");
        exploreSection2.setHeaderTitle("Recommended Rooms");
        exploreSection3.setHeaderTitle("Just Met You");

        // people
        int[] people={R.drawable.img1,R.drawable.img2,R.drawable.img3};

        // set gifts
        ArrayList<Gift> gifts = new ArrayList<>();
        gifts.add(new Gift(R.drawable.orange,R.drawable.rg,"Room Gifts Sent",people));
        gifts.add(new Gift(R.drawable.green,R.drawable.gs,"Gifts Sent",people));
        gifts.add(new Gift(R.drawable.blue,R.drawable.gr,"Gifts Received",people));

        exploreSection.setGifts(gifts);

        // set country
        ArrayList<Country> countries=new ArrayList<>();
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));
        countries.add(new Country("KSA"));

        exploreSection1.setCountries(countries);

        // set activities
        ArrayList<ActivitiesHashTag> activitiesHashTags=new ArrayList<>();
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));

        exploreSection1.setActivities(activitiesHashTags);

        // set sections
        exploreSections.add(exploreSection);
        exploreSections.add(exploreSection1);
        exploreSections.add(exploreSection2);
        exploreSections.add(exploreSection3);


        ExploreRecyclerViewSectionAdapter exploreRecyclerViewSectionAdapter= new ExploreRecyclerViewSectionAdapter(getContext(), this,getActivity());
        exploreRecyclerViewSectionAdapter.setExploreSections(exploreSections);
        recyclerView.setAdapter(exploreRecyclerViewSectionAdapter);
        exploreRecyclerViewSectionAdapter.notifyDataSetChanged();

        return inflatedView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}
