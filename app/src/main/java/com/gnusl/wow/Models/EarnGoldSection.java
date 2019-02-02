package com.gnusl.wow.Models;

import java.util.ArrayList;

public class EarnGoldSection {

    private String headerTitle;
    private ArrayList<EarnGoldTask> earnGoldTasks;

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<EarnGoldTask> getEarnGoldTasks() {
        return earnGoldTasks;
    }

    public void setEarnGoldTasks(ArrayList<EarnGoldTask> earnGoldTasks) {
        this.earnGoldTasks = earnGoldTasks;
    }
}
