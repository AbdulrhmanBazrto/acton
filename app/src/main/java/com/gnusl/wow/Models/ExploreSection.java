package com.gnusl.wow.Models;

import java.util.ArrayList;

/**
 * Created by Yehia on 9/29/2018.
 */

public class ExploreSection {

    private String headerTitle;
    private ArrayList<Gift> gifts;
    private ArrayList<Country> countries;
    private ArrayList<ActivitiesHashTag> activities;
    private ArrayList<Room> rooms;

    public ExploreSection(){}

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public ArrayList<ActivitiesHashTag> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<ActivitiesHashTag> activities) {
        this.activities = activities;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
