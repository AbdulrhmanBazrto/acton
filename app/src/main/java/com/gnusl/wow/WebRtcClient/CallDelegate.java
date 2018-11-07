package com.gnusl.wow.WebRtcClient;

public interface CallDelegate {

    public void makeCallTO(String callerId);

    public void onReadyToCall(String callId);
}
