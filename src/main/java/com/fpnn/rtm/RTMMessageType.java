package com.fpnn.rtm;

public enum RTMMessageType {

    Chat((byte)30),
    Cmd((byte)32),
    ImageFile((byte)40),
    AudioFile((byte)41),
    VideoFile((byte)42),
    NormalFile((byte)50);

    private final byte value;

    RTMMessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
}
