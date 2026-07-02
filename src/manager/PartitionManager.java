package src.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


public class PartitionManager {

    private final Map<Integer, Integer> partitionOwners = new ConcurrentHashMap<>();

    public PartitionManager(int totalPartitions) {
        for (int i = 0; i < totalPartitions; i++)
        {
            partitionOwners.put(i, i);
        }
    }

    public int getOwner(int partition) 
    {
        return partitionOwners.get(partition);
    }

    public void assignPartition(int partition, int worker)
    {
        partitionOwners.put(partition, worker);
    }

    public int getTotalPartitions() 
    {
        return partitionOwners.size();
    }
}
