package com.fpnn.rtm;

import java.net.InetSocketAddress;

public interface RTMClientWillCloseCallback {
    void connectionWillClose(InetSocketAddress peerAddress, boolean causedByError);
}
