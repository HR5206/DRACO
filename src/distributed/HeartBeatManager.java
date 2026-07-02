package src.distributed;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartBeatManager {

    private final Map<Integer, Long> heartbeats = new ConcurrentHashMap<>();

    public void beat(int workerId) {
        heartbeats.put(workerId, System.currentTimeMillis());
    }
}
