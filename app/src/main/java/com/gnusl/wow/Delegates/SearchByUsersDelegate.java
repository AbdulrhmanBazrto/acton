package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.User;

import java.util.ArrayList;

public interface SearchByUsersDelegate {

    void onSearchResultDone(ArrayList<User> users);
}
