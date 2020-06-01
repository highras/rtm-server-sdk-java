package com.fpnn.rtm;

import java.net.InetSocketAddress;

public interface RTMClientConnectCallback {
    void connectResult(InetSocketAddress peerAddress, boolean connected, boolean reConnect, String reConnectInfo);
}
