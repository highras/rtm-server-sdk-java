package com.fpnn.rtm;

public class RTMRegressiveConnectStrategy {

    public int connectFailedMaxIntervalMilliseconds = 1500;  // 1.5seconds 从连接到断开多少秒内算连接失败
    public int startConnectFailedCount = 5;   // 连接失败多少次后，开始退行性处理
    public int firstIntervalSeconds = 2;      // 第一次退行性间隔时间基数
    public int maxIntervalSeconds = 120;      // 退行性重连最大时间间隔
    public int linearRegressiveCount = 5;     // 从第一次退行性连接开始，到最大链接时间，允许尝试几次连接，每次时间间隔都会增大
}
