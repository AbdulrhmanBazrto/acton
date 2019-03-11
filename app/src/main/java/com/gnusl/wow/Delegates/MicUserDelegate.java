package com.gnusl.wow.Delegates;

import com.gnusl.wow.Models.MicUser;

public interface MicUserDelegate {

    public void onTakeMic(MicUser micId);

    public void onSelectUserOnMic(MicUser micUser);
}
