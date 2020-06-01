package com.fpnn.rtm;

public enum MType {

    Chat((byte)30),
    AudioChat((byte)31),
    Cmd((byte)32),
    Picture((byte)40),
    Audio((byte)41),
    Video((byte)42),
    File((byte)50);

    private final byte value;

    MType (byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
}
