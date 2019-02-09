package com.gnusl.wow.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.wow.Adapters.ExploreRecyclerViewSectionAdapter;
import com.gnusl.wow.Connection.APIConnectionNetwork;
import com.gnusl.wow.Delegates.ConnectionDelegate;
import com.gnusl.wow.Models.ActivitiesHashTag;
import com.gnusl.wow.Models.Country;
import com.gnusl.wow.Models.ExploreSection;
import com.gnusl.wow.Models.TopGiftExplorer;
import com.gnusl.wow.Models.Room;
import com.gnusl.wow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Yehia on 9/27/2018.
 */

public class ExploreRoomsFragment extends Fragment implements ConnectionDelegate {

    private View inflatedView;
    private ExploreRecyclerViewSectionAdapter exploreRecyclerViewSectionAdapter;
    private ProgressDialog progressDialog;

    public ExploreRoomsFragment() {
    }

    public static ExploreRoomsFragment newInstance() {

        return new ExploreRoomsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_explore_rooms, container, false);


        RecyclerView recyclerView = inflatedView.findViewById(R.id.explore_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        exploreRecyclerViewSectionAdapter = new ExploreRecyclerViewSectionAdapter(getContext(), this, getActivity());
        exploreRecyclerViewSectionAdapter.setExploreSections(getExploreTempData());
        recyclerView.setAdapter(exploreRecyclerViewSectionAdapter);
        exploreRecyclerViewSectionAdapter.notifyDataSetChanged();

        return inflatedView;
    }


    private void sendExploreDataRequest() {

        // make progress dialog
        this.progressDialog = ProgressDialog.show(getContext(), "", "loading..");

        // send request
        APIConnectionNetwork.GetExploreContent(this);
    }

    private ArrayList<ExploreSection> getExploreTempData() {

        // set Data
        ArrayList<ExploreSection> exploreSections = new ArrayList<>();


        ExploreSection exploreSection = new ExploreSection();
        ExploreSection exploreSection1 = new ExploreSection();
        ExploreSection exploreSection2 = new ExploreSection();
        ExploreSection exploreSection3 = new ExploreSection();

        exploreSection.setHeaderTitle("Top");
        exploreSection1.setHeaderTitle("Countries");
        exploreSection2.setHeaderTitle("Recommended Rooms");
        exploreSection3.setHeaderTitle("Just Met You");

        // people
        int[] people = {R.drawable.img1, R.drawable.img2, R.drawable.img3};

        // set topGiftExplorers
        ArrayList<TopGiftExplorer> topGiftExplorers = new ArrayList<>();
        topGiftExplorers.add(new TopGiftExplorer(R.drawable.orange, R.drawable.rg, "Room Gifts Sent", people));
        topGiftExplorers.add(new TopGiftExplorer(R.drawable.green, R.drawable.gs, "Gifts Sent", people));
        topGiftExplorers.add(new TopGiftExplorer(R.drawable.blue, R.drawable.gr, "Gifts Received", people));

        exploreSection.setTopGiftExplorers(topGiftExplorers);

        // set country
        ArrayList<Country> countries = new ArrayList<>();
        exploreSection1.setCountries(countries);

        // set activities
        ArrayList<ActivitiesHashTag> activitiesHashTags = new ArrayList<>();
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));
        activitiesHashTags.add(new ActivitiesHashTag("Sports"));

        exploreSection1.setActivities(activitiesHashTags);

        // set rooms
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());

        exploreSection2.setRooms(rooms);
        exploreSection3.setRooms(rooms);

        // set sections
        exploreSections.add(exploreSection);
       /* exploreSections.add(exploreSection1);
        exploreSections.add(exploreSection2);
        exploreSections.add(exploreSection3);*/

        return exploreSections;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null && exploreRecyclerViewSectionAdapter!=null) {

            sendExploreDataRequest();
        }
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

        ArrayList<ExploreSection> exploreSections = new ArrayList<>();

        // add top
        exploreSections.addAll(getExploreTempData());

        // parsing countries
        ExploreSection countiesExploreSection = new ExploreSection();
        countiesExploreSection .setHeaderTitle("Countries");
        if (jsonObject.has("country_codes")) {
            try {
                countiesExploreSection .setCountries(Country.parseJSONArray(jsonObject.getJSONArray("country_codes")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // parsing tags
        if (jsonObject.has("tags")) {
            try {
                JSONArray tagsJsonArray = jsonObject.getJSONArray("tags");
                ArrayList<ActivitiesHashTag> hashTags = new ArrayList<>();
                for (int i = 0; i < tagsJsonArray .length(); i++)
                    hashTags.add(new ActivitiesHashTag(tagsJsonArray .getString(i)));

                countiesExploreSection .setActivities(hashTags);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        exploreSections.add(countiesExploreSection);

        // recommended rooms
        if (jsonObject.has("recomended")) {

            try {
                ArrayList<Room> room=Room.parseJSONArray(jsonObject.getJSONArray("recomended"));
                ExploreSection recommendedExploreSection=new ExploreSection();
                recommendedExploreSection.setHeaderTitle("Recommended Rooms");
                recommendedExploreSection.setRooms(room);

                exploreSections.add(recommendedExploreSection);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // for you rooms
        if (jsonObject.has("for_you")) {

            try {
                ArrayList<Room> room=Room.parseJSONArray(jsonObject.getJSONArray("for_you"));
                ExploreSection forYouExploreSection=new ExploreSection();
                forYouExploreSection.setHeaderTitle("Just Met You");
                forYouExploreSection.setRooms(room);

                exploreSections.add(forYouExploreSection);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        // refresh
        exploreRecyclerViewSectionAdapter.setExploreSections(exploreSections);
        exploreRecyclerViewSectionAdapter.notifyDataSetChanged();

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onConnectionSuccess(JSONArray jsonArray) {

    }
}
