package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.User;

import java.util.ArrayList;

public interface SearchByUsersDelegate {

    public void onSearchResultDone(ArrayList<User> users);
}
