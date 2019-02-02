package com.gnusl.wow.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gnusl.wow.Adapters.EarnGoldSectionAdapter;
import com.gnusl.wow.Models.EarnGoldSection;
import com.gnusl.wow.Models.EarnGoldTask;
import com.gnusl.wow.R;

import java.util.ArrayList;

public class EarnGoldActivity extends AppCompatActivity {

    private EarnGoldSectionAdapter earnGoldSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_gold);

        findViewById(R.id.back_button).setOnClickListener(v->onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.earn_gold_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        EarnGoldSection earnGoldSection = new EarnGoldSection();
        earnGoldSection.setHeaderTitle("المهمات اليومية");

        ArrayList<EarnGoldTask> earnGoldTasks = new ArrayList<>();
        earnGoldTasks.add(new EarnGoldTask());
        earnGoldTasks.add(new EarnGoldTask());

        earnGoldSection.setEarnGoldTasks(earnGoldTasks);

        ArrayList<EarnGoldSection> earnGoldSections = new ArrayList<>();
        earnGoldSections.add(earnGoldSection);

        earnGoldSectionAdapter = new EarnGoldSectionAdapter(this);
        earnGoldSectionAdapter.setEarnGoldSections(earnGoldSections);
        recyclerView.setAdapter(earnGoldSectionAdapter);
        earnGoldSectionAdapter.notifyDataSetChanged();

    }
}
