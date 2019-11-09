package com.test;

import com.rtm.RTMClient;

public class TestMain {

    public static void main(String[] args) {
        System.out.println(new String("rtm test with main!"));

        RTMClient.RTMRegistration.register();
        baseTest();
    }

    static void baseTest() {
        new TestCase();
    }
}
