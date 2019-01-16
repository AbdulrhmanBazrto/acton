package com.gnusl.wow.WebRtcClient;

public interface CallDelegate {

    void makeCallTO(String callerId);

    void onReadyToCall(String callId);
}
