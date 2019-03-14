package com.gnusl.wow.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gnusl.wow.Delegates.ChooseFAQDelegate;
import com.gnusl.wow.Models.Badge;
import com.gnusl.wow.Models.FAQ;
import com.gnusl.wow.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FAQTabsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ChooseFAQDelegate chooseFAQDelegate;
    private Context context;
    private List<FAQ> faqs;
    private int selectedPos = 0;

    public FAQTabsRecyclerViewAdapter(Context context, ArrayList<FAQ> faqs, ChooseFAQDelegate chooseFAQDelegate) {
        this.context = context;
        this.faqs = faqs;
        this.chooseFAQDelegate = chooseFAQDelegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.faq_tab_view_holder, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((BadgeViewHolder) holder).bind(getFaqs().get(position), position);

    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    public class BadgeViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private AppCompatImageView ivFaqImage;

        public BadgeViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            ivFaqImage = itemView.findViewById(R.id.iv_faq_image);

        }

        public void bind(final FAQ faq, final int position) {


            if (selectedPos == position) {
                itemView.setBackgroundColor(context.getResources().getColor(R.color.gray_color_80));
            } else {
                itemView.setBackgroundResource(R.drawable.bg_shape_faq_tabs);
            }


            Picasso.with(context).load(faq.getImageUrl()).into(ivFaqImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chooseFAQDelegate != null)
                        chooseFAQDelegate.onSelectFAQ(faq);
                    selectedPos = position;
                    notifyDataSetChanged();
                }
            });


        }

    }

    // region setters and getters

    public List<FAQ> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FAQ> faqs) {
        this.faqs = faqs;
        notifyDataSetChanged();
    }


    // endregion
}

