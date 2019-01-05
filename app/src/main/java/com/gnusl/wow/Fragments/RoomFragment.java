package com.gnusl.wow.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gnusl.wow.Activities.MainActivity;
import com.gnusl.wow.Activities.SearchActivity;
import com.gnusl.wow.Adapters.RoomFragmentPagerAdapter;
import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Popups.GiftsRoomDialog;
import com.gnusl.wow.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yehia on 9/23/2018.
 */

public class RoomFragment extends Fragment implements SmartTabLayout.TabProvider {

    private View inflatedView;

    public RoomFragment() {
    }

    public static RoomFragment newInstance() {

        return new RoomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_room, container, false);


        // initialize top bar with viewpager
        initializeSmartTabs();

        // open drawer
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.right_icon).setOnClickListener(v->{

                try {
                    JSONArray jsonArray=new JSONArray(" [\n" +
                            "        {\n" +
                            "            \"id\": 1,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/1.png\",\n" +
                            "            \"price\": 22\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 2,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/2.png\",\n" +
                            "            \"price\": 23\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 3,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/3.png\",\n" +
                            "            \"price\": 33\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 4,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/4.png\",\n" +
                            "            \"price\": 44\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 5,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/5.png\",\n" +
                            "            \"price\": 55\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 6,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/6.png\",\n" +
                            "            \"price\": 66\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 7,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/7.png\",\n" +
                            "            \"price\": 77\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 8,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/8.png\",\n" +
                            "            \"price\": 88\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 9,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/9.png\",\n" +
                            "            \"price\": 99\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 10,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/10.png\",\n" +
                            "            \"price\": 100\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": 11,\n" +
                            "            \"path\": \"http://fat7al.com/wow/public/uploads/gifts/11.png\",\n" +
                            "            \"price\": 110\n" +
                            "        }\n" +
                            "    ]");

                    GiftsRoomDialog.show(getContext(),Gift.parseJSONArray(jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //((MainActivity) getActivity()).getDrawer().openDrawer(GravityCompat.START);
            });

        // open search
        if (getActivity() instanceof MainActivity)
            inflatedView.findViewById(R.id.search_icon).setOnClickListener(v->{

                startActivity(new Intent(getActivity(),SearchActivity.class));
            });

        return inflatedView;
    }

    private void initializeSmartTabs() {

        RoomFragmentPagerAdapter roomFragmentPagerAdapter = new RoomFragmentPagerAdapter(getContext(), getFragmentManager(), null);

        ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.roomviewpager);
        viewPager.setAdapter(roomFragmentPagerAdapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) inflatedView.findViewById(R.id.viewpagertab);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View inflatedView = inflater.inflate(R.layout.custom_room_tab_bar_item_view, container, false);

        TextView fontedTextView = (TextView) inflatedView.findViewById(R.id.title);

        switch (position) {

            case 0:
                fontedTextView.setText(getString(R.string.new_text));
                break;

            case 1:

                fontedTextView.setText(getString(R.string.all));
                break;

            case 2:

                fontedTextView.setText(getString(R.string.explore));
                break;

        }

        return inflatedView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && inflatedView != null) {

        }
    }

}

