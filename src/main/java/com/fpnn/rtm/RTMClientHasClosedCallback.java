package com.fpnn.rtm;

import java.net.InetSocketAddress;

public interface RTMClientHasClosedCallback {
    void connectionHasClosed(InetSocketAddress peerAddress, boolean causedByError, boolean reConnect, String reConnectInfo);
}
