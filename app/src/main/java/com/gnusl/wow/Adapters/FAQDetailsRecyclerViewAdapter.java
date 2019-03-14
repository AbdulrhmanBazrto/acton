package com.gnusl.wow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.wow.Activities.FAQAnswerActivity;
import com.gnusl.wow.Delegates.ChooseFAQDelegate;
import com.gnusl.wow.Models.FAQ;
import com.gnusl.wow.Models.FAQDetails;
import com.gnusl.wow.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FAQDetailsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<FAQDetails> faqs;

    public FAQDetailsRecyclerViewAdapter(Context context, ArrayList<FAQDetails> faqs) {
        this.context = context;
        this.faqs = faqs;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.faq_question_view_holder, parent, false);
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
        private TextView tvFaqTitle;

        public BadgeViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            tvFaqTitle = itemView.findViewById(R.id.tv_faq_title);

        }

        public void bind(final FAQDetails faq, final int position) {

            tvFaqTitle.setText(faq.getQuestion());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FAQAnswerActivity.class);
                    intent.putExtra("faq", faq);
                    context.startActivity(intent);
                }
            });


        }

    }

    // region setters and getters

    public List<FAQDetails> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FAQDetails> faqs) {
        this.faqs = faqs;
        notifyDataSetChanged();
    }


    // endregion
}

