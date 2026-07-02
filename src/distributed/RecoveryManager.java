package src.distributed;

import src.manager.PartitionManager;

public class RecoveryManager {
    
    private final PartitionManager partitionManager;

    public RecoveryManager(PartitionManager partitionManager) 
    {
        this.partitionManager = partitionManager;
    }

    public void recover(int failedWorker) 
    {
        System.out.println("Worker " + failedWorker + " failed.");

        for (int partition = 0; partition < partitionManager.getTotalPartitions(); partition++)
        {
            if (partitionManager.getOwner(partition) == failedWorker) 
            {
                partitionManager.assignPartition(partition, 0);

                System.out.println("Partition " + partition + " moved to Worker 0");
            }
        }
    }
}
