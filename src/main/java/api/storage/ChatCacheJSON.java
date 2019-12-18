package api.storage;

import java.util.HashMap;
import java.util.Map;

public class ChatCacheJSON extends Chat<Map<Integer, User>> {
    public ChatCacheJSON(int peerId) {
        super(new HashMap<>(), peerId);
    }
}
