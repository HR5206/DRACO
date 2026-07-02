package src.distributed;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import src.model.MarketTick;
import src.service.PortfolioManager;

public class WorkerNode extends Thread {
    private final int workerId;
    private final PortfolioManager portfolioManager;
    private BlockingQueue<MarketTick> queue = new LinkedBlockingQueue<MarketTick>();
    private final HeartBeatManager heartBeatManager;

    public WorkerNode(int workerId, PortfolioManager portfolioManager) {
        this.workerId = workerId;
        this.portfolioManager = portfolioManager;
        this.heartBeatManager = new HeartBeatManager();
    }

    public void addTick(MarketTick tick) {
        try{
            heartBeatManager.beat(workerId);
            queue.put(tick);
        }
        catch (InterruptedException e)  {
            Thread.currentThread().interrupt();
        }   
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                MarketTick tick = queue.take();
                System.out.println("Worker " + workerId + " processing " + tick.getSymbol());              
                portfolioManager.processTick(tick);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}
