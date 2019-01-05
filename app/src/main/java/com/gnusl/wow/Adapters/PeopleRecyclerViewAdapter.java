package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.wow.R;

/**
 * Created by Yehia on 9/29/2018.
 */

public class PeopleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int[] people;

    public PeopleRecyclerViewAdapter(Context context, int[] people) {
        this.context = context;
        this.people=people;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.person_view_holder, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((PersonViewHolder) holder).bind(people[position]);

    }

    @Override
    public int getItemCount() {
        return people.length;
    }

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView personImageView;

        public PersonViewHolder(View itemView) {
            super(itemView);

            personImageView=(AppCompatImageView)itemView.findViewById(R.id.person_image);
        }

        public void bind(final int imageResource) {

            // person image
            personImageView.setImageResource(imageResource);

        }

    }
}


