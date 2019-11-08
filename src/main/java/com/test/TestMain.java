package com.test;

import com.rtm.RTMClient;

public class TestMain {

    public static void main(String[] args) {
        RTMClient.RTMRegistration.register();
        System.out.println(new String("rtm test with main!"));
        // case 1
        baseTest();
    }

    public static void baseTest() {
        new TestCase();
    }
}
