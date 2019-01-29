package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.Room;

import java.util.ArrayList;

public interface SearchByRoomDelegate {

    void onSearchResultDone(ArrayList<Room> rooms);
}
