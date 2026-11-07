package src.distributed;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import src.model.MarketTick;
import src.service.PortfolioManager;

/** A local worker thread. closeAndJoin drains accepted ticks before stopping. */
public class WorkerNode extends Thread {
    private static final MarketTick STOP = new MarketTick("STOP", 0, 0, 0);
    private final BlockingQueue<MarketTick> queue = new LinkedBlockingQueue<>(1024);
    private final PortfolioManager manager;
    private boolean closed;
    private volatile RuntimeException failure;

    public WorkerNode(int id, PortfolioManager manager) {
        super("draco-worker-" + id);
        this.manager = manager;
    }

    public synchronized void addTick(MarketTick tick) {
        if (closed) throw new IllegalStateException("Worker is closed");
        if (tick == null) throw new IllegalArgumentException("Tick is required");
        enqueue(tick);
    }

    private void enqueue(MarketTick tick) {
        try { queue.put(tick); }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while submitting a tick", e);
        }
    }

    public void closeAndJoin() throws InterruptedException {
        synchronized (this) {
            if (!closed) { closed = true; enqueue(STOP); }
        }
        join();
        if (failure != null) throw failure;
    }

    @Override public void run() {
        try {
            while (true) {
                MarketTick tick = queue.take();
                if (tick == STOP) return;
                // Drain even after a failed valuation so producers cannot deadlock.
                if (failure == null) {
                    try { manager.processTick(tick); }
                    catch (RuntimeException e) { failure = e; }
                }
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
