package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Activities.RoomByCountryActivity;
import com.gnusl.wow.Models.Country;
import com.gnusl.wow.Models.Country;
import com.gnusl.wow.R;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Country> countries;

    public CountryRecyclerViewAdapter(Context context, ArrayList<Country> countries) {
        this.context = context;
        this.countries=countries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.country_view_holder, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((CountryViewHolder) holder).bind(getCountries().get(position), position);

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public CountryViewHolder(View itemView) {
            super(itemView);

            nameTextView= itemView.findViewById(R.id.name_text);
        }

        public void bind(final Country country, final int position) {

            // content
            nameTextView.setText(country.getName());

            // go to room filter
            itemView.setOnClickListener(v->{

                RoomByCountryActivity.show(context,country.getName());
            });

        }

    }

    // region setters and getters

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }


    // endregion
}


