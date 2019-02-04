package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.MicUser;

public interface MicUserDelegate {

    public void onTakeMic(int micId);

    public void onSelectUserOnMic(MicUser micUser);
}
