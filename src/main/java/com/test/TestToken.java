package com.test;

import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;

import java.util.Map;

public class TestToken {

    static RTMClient client;
    public static void main(String[] args) {
        System.out.println(new String("rtm test with main!"));

        RTMClient.RTMRegistration.register();

        client = new RTMClient(
                11000001,
                "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "52.83.245.22:13315",
                true,
                20 * 1000,
                true
        );
        client.getEvent().addListener("connect", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                System.out.println(new String("Connected!"));
                getToken();
            }
        });
        client.getEvent().addListener("close", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                System.out.println(new String("Closed!"));
                RTMClient.RTMRegistration.unregister();
            }
        });
        client.getEvent().addListener("error", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                evd.getException().printStackTrace();
            }
        });
        client.connect();
    }

    static void getToken() {
        client.getToken(777779, 20 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();
                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("token: " + payload.toString());
                } else {
                    System.err.println(cbd.getException());
                }
                client.destroy();
            }
        });
    }
}
