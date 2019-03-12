package com.gnusl.wow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.gnusl.wow.Models.User;
import com.gnusl.wow.Models.Visit;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.TimeAgo;
import com.gnusl.wow.Views.AutoFitFontedTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VisitorsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Visit> visitors;

    public VisitorsRecyclerViewAdapter(Context context, ArrayList<Visit> visitors) {
        this.context = context;
        this.visitors = visitors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.visitors_view_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((UserViewHolder) holder).bind(getVisitors().get(position), position);

    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private CircularImageView profile_image;
        private AutoFitFontedTextView user_name;
        private AutoFitFontedTextView date;

        public UserViewHolder(View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.user_name);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(final Visit user, final int position) {

            if (user != null) {

                // name
                if (!user.getName().isEmpty())
                    user_name.setText(user.getName());

                // giftUserRank image
                if (user.getImage_url() != null && !user.getImage_url().isEmpty())
                    Glide.with(context)
                            .load(user.getImage_url())
                            .into(profile_image);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                try {
                    Date parse = simpleDateFormat.parse(user.getDate());
                    date.setText(TimeAgo.getTimeAgo(parse.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }


    }

    // region setters and getters

    public ArrayList<Visit> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visit> followers) {
        this.visitors = followers;
    }

    // endregion
}
