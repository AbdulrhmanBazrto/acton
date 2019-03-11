package com.gnusl.wow.Delegates;

import com.gnusl.wow.Enums.UserRoomActions;
import com.gnusl.wow.Models.User;


public interface UserRoomActionsDelegate {

    void onActionClick(UserRoomActions userRoomActions,User user);
}

