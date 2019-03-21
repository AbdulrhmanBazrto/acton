package com.gnusl.wow.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gnusl.wow.Models.FAQ;
import com.gnusl.wow.Models.FAQDetails;
import com.gnusl.wow.R;
import com.gnusl.wow.Utils.LocaleManager;

import java.io.Serializable;

public class FAQAnswerActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqanswer);

        FAQDetails faq = (FAQDetails) getIntent().getSerializableExtra("faq");

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView title = findViewById(R.id.title);
        TextView answer = findViewById(R.id.tv_faq_answer);
        title.setText(faq.getQuestion());


        answer.setText(faq.getAnswer());

    }
}
