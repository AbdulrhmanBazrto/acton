package com.gnusl.wow.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gnusl.wow.Models.User;

import java.util.List;

public class UsersSpinnerAdapter extends ArrayAdapter<Object> {


    public UsersSpinnerAdapter(Context context, int resource, List<Object> items) {
        super(context, resource, items);

    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);

        // handle object type to show it
        Object object = super.getItem(position);
        if (object instanceof String)
            view.setText(((String) object));
        else if (object instanceof User)
            view.setText(((User) object).getName());

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;

    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        // handle object type to show it
        Object object = super.getItem(position);
        if (object instanceof String)
            view.setText(((String) object));
        else if (object instanceof User)
            view.setText(((User) object).getName());

        return view;
    }
}