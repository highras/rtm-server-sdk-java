package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMMessageType;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessage;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessageUnit;
import com.fpnn.rtm.RTMServerClientBase.RTMMessage;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Set;


interface MessageCoreAPI extends APIBase {

    //-----------------[ sendmsg ]-----------------//
    default void internalCoreSendMessage(long fromUid, long toUid, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("sendmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate P2P message sign exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate P2P message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("to", toUid);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                    callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(-1, i, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default long internalCoreSendMessage(long fromUid, long toUid, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("sendmsg");
        quest.param("from", fromUid);
        quest.param("to", toUid);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getLong("mtime", 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    default void internalCoreSendMessages(long fromUid, Set<Long> toUids, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("sendmsgs");
        } catch (Exception e) {
            ErrorRecorder.record("Generate multi-receiver message sign exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate multi-receiver message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("tos", toUids);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(-1, i, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default long internalCoreSendMessages(long fromUid, Set<Long> toUids, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("sendmsgs");
        quest.param("from", fromUid);
        quest.param("tos", toUids);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getLong("mtime", 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    default void internalCoreSendGroupMessage(long fromUid, long groupId, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("sendgroupmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate group message sign exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate group message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("gid", groupId);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                    callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(-1, i, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default long internalCoreSendGroupMessage(long fromUid, long groupId, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("sendgroupmsg");
        quest.param("from", fromUid);
        quest.param("gid", groupId);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

       Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
       return answer.getLong("mtime", 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    default void internalCoreSendRoomMessage(long fromUid, long roomId, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("sendroommsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate room message sign exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate room message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("rid", roomId);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                    callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex","");
                callback.done(-1, i, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default long internalCoreSendRoomMessage(long fromUid, long roomId, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("sendroommsg");
        quest.param("from", fromUid);
        quest.param("rid", roomId);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        Answer answer =client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getLong("mtime", 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    default void internalCoreSendBroadcastMessage(long fromUid, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("broadcastmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate broadcast message sign exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate broadcast message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                    callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(-1, i, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default long internalCoreSendBroadcastMessage(long fromUid, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("broadcastmsg");
        quest.param("from", fromUid);
        quest.param("mid", genMid());
        quest.param("mtype", messageType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getLong("mtime", 0);
    }

    default RTMHistoryMessage buildHistoryMessages(Answer answer){
        RTMHistoryMessage result = new RTMHistoryMessage();
        List<Object> messages = (List<Object>)answer.get("msgs", null);
        if(messages == null)
            return result;
        result.count = answer.getInt("num", 0);
        result.lastCursorId = answer.getLong("lastid", 0);
        result.beginMsec = answer.getLong("begin", 0);
        result.endMsec = answer.getLong("end", 0);
        for (int i = 0; i < messages.size(); i++) {
            List<Object> data = (List<Object>) messages.get(i);
            RTMHistoryMessageUnit unitMsg = new RTMHistoryMessageUnit();
            boolean delete = (boolean)(data.get(4));
            if(delete)
                continue;

            unitMsg.cursorId = Long.valueOf(String.valueOf(data.get(0)));
            RTMMessage info = new RTMMessage();
            info.fromUid = Long.valueOf(String.valueOf(data.get(1)));
            info.messageType = Byte.valueOf(String.valueOf(data.get(2)));
            info.messageId = Long.valueOf(String.valueOf(data.get(3)));
            //jump delete
            info.attrs = String.valueOf(data.get(6));
            info.modifiedTime = Long.valueOf(String.valueOf(data.get(7)));
            Object obj = data.get(5);
            if(info.messageType >= RTMMessageType.ImageFile.value() && info.messageType <= RTMMessageType.NormalFile.value()){
                try{
                    String msg = String.valueOf(obj);
                    info.fileMsgInfo = RTMServerClientBase.processFileInfo(msg, info.attrs, info.messageType);
                    info.attrs = RTMServerClientBase.fetchFileCustomAttrs(info.attrs);

                }catch (Exception ex){
                    ex.printStackTrace();
                    ErrorRecorder.record("unknown error file json string parse failed", ex);
                }
            }
            else{
                info.stringMessage = String.valueOf(obj);
            }
            unitMsg.message = info;
            result.messageList.add(unitMsg);
        }
        result.count = result.messageList.size();
        return result;
    }

    //-----------------getGroupHistoryMessage-----------------
    default RTMHistoryMessage internalCoreGetGroupHistoryMsg(long uid, long groupId, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("getgroupmsg");
        quest.param("uid", uid);
        quest.param("gid", groupId);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return buildHistoryMessages(answer);
    }

    default void internalCoreGetGroupHistoryMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getgroupmsg");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate getgroupmsg message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getgroupmsg message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("gid", gid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                {
                    RTMHistoryMessage result = buildHistoryMessages(answer);
                    callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //-----------------getRoomHistoryMessage-----------------
    default RTMHistoryMessage internalCoreGetRoomHistoryMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("getroommsg");
        quest.param("uid", uid);
        quest.param("rid", rid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return buildHistoryMessages(answer);
    }

    default void internalCoreGetRoomHistoryMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getroommsg");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate getroommsg message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getroommsg message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("rid", rid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                {
                    RTMHistoryMessage result = buildHistoryMessages(answer);
                    callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex","");
                callback.done(null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //-----------------getBroadCastHistoryMessage-----------------
    default RTMHistoryMessage internalCoreGetBroadCastHistoryMsg(long uid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("getbroadcastmsg");
        quest.param("uid", uid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return buildHistoryMessages(answer);
    }

    default void internalCoreGetBroadCastHistoryMsg(long uid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getbroadcastmsg");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate getbroadcastmsg message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getbroadcastmsg message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                {
                    RTMHistoryMessage result = buildHistoryMessages(answer);
                    callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void adjustHistoryMessageForP2P(RTMHistoryMessage message, long from, long peer){
        for (RTMHistoryMessageUnit unit: message.messageList) {
            if(unit.message != null){
                if(unit.message.fromUid == 1){
                    unit.message.fromUid = from;
                }
                else if(unit.message.fromUid == 2){
                    unit.message.fromUid = peer;
                }
            }
        }
    }

    //-----------------getP2PHistoryMessage-----------------
    default RTMHistoryMessage internalCoreGetP2PHistoryMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("getp2pmsg");
        quest.param("uid", uid);
        quest.param("ouid", peerUid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        RTMHistoryMessage result =  buildHistoryMessages(answer);
        adjustHistoryMessageForP2P(result, uid, peerUid);
        return result;
    }

    default void internalCoreGetP2PHistoryMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getp2pmsg");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate getp2pmsg message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getp2pmsg message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("ouid", peerUid);
        quest.param("desc", desc);
        quest.param("num", count);
        quest.param("begin", begin);
        quest.param("end", end);
        quest.param("lastid", lastCursorId);
        if(mtypes != null && mtypes.size() > 0)
            quest.param("mtypes", mtypes);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                {
                    RTMHistoryMessage result = buildHistoryMessages(answer);
                    adjustHistoryMessageForP2P(result, uid, peerUid);
                    callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done(null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //----------------------delMsg------------------
    default void internalDelMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("delmsg");
        quest.param("mid", mid);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void internalDelMsg(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("delmsg");
        }
        catch (Exception ex)
        {
            ErrorRecorder.record("Generate delmsg message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delmsg message sign exception.");
            return;
        }
        quest.param("mid", mid);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //----------------------getMsg------------------

    default RTMHistoryMessageUnit buildRetrievedMessage(Answer answer){
        RTMHistoryMessageUnit message = new RTMHistoryMessageUnit();
        RTMMessage info = new RTMMessage();
        message.cursorId = answer.getLong("id", 0);
        info.messageType = ((Integer)answer.getInt("mtype", 0)).byteValue();
        info.attrs = (String)answer.get("attrs", "");
        info.modifiedTime = answer.getLong("mtime", 0);
        String msg = (String)answer.get("msg", "");
        if(info.messageType >= RTMMessageType.ImageFile.value() && info.messageType <= RTMMessageType.NormalFile.value()){
            try{
                info.fileMsgInfo = RTMServerClientBase.processFileInfo(msg, info.attrs, info.messageType);
                info.attrs = RTMServerClientBase.fetchFileCustomAttrs(info.attrs);

            }catch (Exception ex){
                ex.printStackTrace();
                ErrorRecorder.record("unknown error file json string parse failed", ex);
            }
        }
        else{
            info.stringMessage = msg;
        }
        message.message = info;
        return  message;
    }

    default RTMHistoryMessageUnit internalGetMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getmsg");
        quest.param("mid", mid);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return buildRetrievedMessage(answer);
    }

    default void internalGetMsg(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getmsg");
        }
        catch (Exception ex)
        {
            ErrorRecorder.record("Generate getmsg message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getmsg message sign exception.");
            return;
        }
        quest.param("mid", mid);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                {
                    RTMHistoryMessageUnit message = buildRetrievedMessage(answer);
                    callback.done(message, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null)
                {
                    info = (String) answer.get("ex", "");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

}
