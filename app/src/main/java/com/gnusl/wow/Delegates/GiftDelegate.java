package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.Gift;
import com.gnusl.wow.Models.User;

public interface GiftDelegate {

    public void onClickToSendGift(Gift gift, User toUser);
}
