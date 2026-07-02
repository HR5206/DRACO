package src.distributed;

public class Partitioner {
    public int getPartition(String symbol, int totalPartitions) {
        return Math.abs(symbol.hashCode() % totalPartitions);
    }
}
