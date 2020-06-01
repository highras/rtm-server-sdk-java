package com.fpnn.rtm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class RTMDuplicatedMessageFilter {

    private class DupP2PMessageKey{
        long sender;
        long receiver;
        long mid;
        DupP2PMessageKey(long from, long to, long id){
            sender = from;
            receiver = to;
            mid = id;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null){
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }
            DupP2PMessageKey other = (DupP2PMessageKey)obj;
            if(sender != other.sender || receiver != other.receiver || mid != other.mid)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 17;
            result = prime * result + (int)(sender^(sender >>>32));
            result = prime * result + (int)(receiver^(receiver >>>32));
            result = prime * result + (int)(mid^(mid >>>32));
            return result;
        }
    }

    private class DupGroupMessageKey{
        long sender;
        long groupId;
        long mid;
        DupGroupMessageKey(long from, long gid, long id){
            sender = from;
            groupId = gid;
            mid = id;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null){
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }
            DupGroupMessageKey other = (DupGroupMessageKey)obj;
            if(sender != other.sender || groupId != other.groupId || mid != other.mid)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 17;
            result = prime * result + (int)(sender^(sender >>>32));
            result = prime * result + (int)(groupId^(groupId >>>32));
            result = prime * result + (int)(mid^(mid >>>32));
            return result;
        }
    }

    private class DupRoomMessageKey{
        long sender;
        long roomId;
        long mid;
        DupRoomMessageKey(long from, long rid, long id){
            sender = from;
            roomId = rid;
            mid = id;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null){
                return false;
            }
            if(getClass() != obj.getClass()){
                return false;
            }
            DupRoomMessageKey other = (DupRoomMessageKey)obj;
            if(sender != other.sender || roomId != other.roomId || mid != other.mid)
            {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = 17;
            result = prime * result + (int)(sender^(sender >>>32));
            result = prime * result + (int)(roomId^(roomId >>>32));
            result = prime * result + (int)(mid^(mid >>>32));
            return result;
        }
    }

    Map<DupP2PMessageKey, Long> p2pCache;
    Map<DupGroupMessageKey, Long> groupCache;
    Map<DupRoomMessageKey, Long> roomCache;
    static final int dupFilterCleanIntervalSeconds = 5 * 60;
    static final int dupFilterTriggerCleanCount = 1000;

    RTMDuplicatedMessageFilter(){
        p2pCache = new HashMap<>();
        groupCache = new HashMap<>();
        roomCache = new HashMap<>();
    }

    synchronized boolean checkP2PMessage(long sender, long receiver, long mid){
        DupP2PMessageKey key = new DupP2PMessageKey(sender, receiver, mid);
        boolean ok = p2pCache.containsKey(key);
        long currTime = System.currentTimeMillis() /1000;
        if(p2pCache.size() > dupFilterTriggerCleanCount){
            long threshold = currTime - dupFilterCleanIntervalSeconds;
            HashSet<DupP2PMessageKey> oldKeys = new HashSet<>();
            for(Map.Entry<DupP2PMessageKey, Long> entry : p2pCache.entrySet()){
                if(entry.getValue() <= threshold){
                    oldKeys.add(entry.getKey());
                }
            }
            for (DupP2PMessageKey value : oldKeys){
                p2pCache.remove(value);
            }
        }
        p2pCache.put(key, currTime);
        return !ok;
    }

    synchronized boolean checkGroupMessage(long sender, long gid, long mid){
        DupGroupMessageKey key = new DupGroupMessageKey(sender, gid, mid);
        boolean ok = groupCache.containsKey(key);
        long currTime = System.currentTimeMillis() /1000;
        if(groupCache.size() > dupFilterTriggerCleanCount){
            long threshold = currTime - dupFilterCleanIntervalSeconds;
            HashSet<DupGroupMessageKey> oldKeys = new HashSet<>();
            for(Map.Entry<DupGroupMessageKey, Long> entry : groupCache.entrySet()){
                if(entry.getValue() <= threshold){
                    oldKeys.add(entry.getKey());
                }
            }
            for (DupGroupMessageKey value : oldKeys){
                groupCache.remove(value);
            }
        }
        groupCache.put(key, currTime);
        return !ok;
    }

    synchronized boolean checkRoomMessage(long sender, long rid, long mid){
        DupRoomMessageKey key = new DupRoomMessageKey(sender, rid, mid);
        boolean ok = roomCache.containsKey(key);
        long currTime = System.currentTimeMillis() /1000;
        if(roomCache.size() > dupFilterTriggerCleanCount){
            long threshold = currTime - dupFilterCleanIntervalSeconds;
            HashSet<DupRoomMessageKey> oldKeys = new HashSet<>();
            for(Map.Entry<DupRoomMessageKey, Long> entry : roomCache.entrySet()){
                if(entry.getValue() <= threshold){
                    oldKeys.add(entry.getKey());
                }
            }
            for (DupRoomMessageKey value : oldKeys){
                roomCache.remove(value);
            }
        }
        roomCache.put(key, currTime);
        return !ok;
    }
}
