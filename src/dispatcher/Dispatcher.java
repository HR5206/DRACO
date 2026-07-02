package src.dispatcher;

import src.distributed.Partitioner;
import src.distributed.WorkerNode;
import src.manager.PartitionManager;
import src.model.MarketTick;

public class Dispatcher {
    private final WorkerNode[] workers;
    private final PartitionManager partitionManager;
    private final Partitioner partitioner;

    public Dispatcher(WorkerNode[] workers, Partitioner partitioner, PartitionManager partitionManager) {
        this.partitioner = partitioner;
        this.workers = workers;
        this.partitionManager = partitionManager;
    }

    public void dispatch(MarketTick tick) 
    {
        int partition = partitioner.getPartition(tick.getSymbol(), workers.length);
        int worker = partitionManager.getOwner(partition);
        workers[worker].addTick(tick);
    }
}
