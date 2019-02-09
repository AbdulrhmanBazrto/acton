package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.Advertisement;
import com.gnusl.wow.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AdvertisementPagerAdapter extends PagerAdapter {

    private ArrayList<Advertisement> advertisements;
    private Context context;

    public AdvertisementPagerAdapter(Context context,ArrayList<Advertisement> advertisements) {
        this.advertisements = advertisements;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View page = inflater.inflate(R.layout.advertisement_view_holder, null);
        
        initializeView(page,position);
        (container).addView(page);
        return page;
    }
    
    private void initializeView(View view,int position){

        ImageView imageView=view.findViewById(R.id.ads_image);
        Advertisement advertisement=getAdvertisements().get(position);

        // room image
        if (advertisement.getImage_url() != null && !advertisement.getImage_url().isEmpty())
            Glide.with(context)
                    .load(advertisement.getImage_url())
                    .into(imageView);

    }

    @Override
    public int getCount() {
        return getAdvertisements().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }

    public ArrayList<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(ArrayList<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }
}
