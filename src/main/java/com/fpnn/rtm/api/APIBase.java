package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.proto.Answer;

public interface APIBase {
    RTMServerClientBase getCoreClient();
    long genMid();

    interface DoneLambdaCallback {
        void done(int errorCode, String message);
    }

    interface SendMessageLambdaCallback{
        void done(long time, int errorCode, String errorMessage);
    }

    interface GetHistoryMessagesLambdaCallback{
        void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
    }

    interface GetRetrievedMessageLambdaCallback{
        void done(RTMServerClientBase.RTMHistoryMessageUnit result, int errorCode, String errorMessage);
    }

    enum MessageType{
        MESSAGE_TYPE_P2P((byte)1),
        MESSAGE_TYPE_GROUP((byte)2),
        MESSAGE_TYPE_ROOM((byte)3),
        MESSAGE_TYPE_BROADCAST((byte)4);

        private final int value;
        MessageType(byte type) {value = type;}

        public int value() {
            return value;
        }
    }

    enum ClearType{
        CLEAR_TYPE_P2P((byte)0),
        CLEAR_TYPE_ROOM((byte)1),
        CLEAR_TYPE_GROUP((byte)2),
        CLEAR_TYPE_BROADCAST((byte)3),
        CLEAR_TYPE_ALL((byte)4);

        private  final int value;
        ClearType(byte type) {value = type;}

        public int value() {return value;}
    }

    //-----------------------------------------------------//
    //--         Utilities classes & interfaces          --//
    //-----------------------------------------------------//
    class FPNNDoneLambdaCallbackWrapper extends AnswerCallback {

        DoneLambdaCallback callback;

        public FPNNDoneLambdaCallbackWrapper(DoneLambdaCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onAnswer(Answer answer) {
            if (callback != null)
                callback.done(ErrorCode.FPNN_EC_OK.value(), "");
        }

        @Override
        public void onException(Answer answer, int errorCode) {
            if (callback != null) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                callback.done(errorCode, info);
            }
        }
    }
}
