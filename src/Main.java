package src;

import src.cache.MarketDataCache;
import src.distributed.*;
import src.dispatcher.Dispatcher;
import src.manager.PartitionManager;
import src.model.*;
import src.service.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        AuditLogger log = new AuditLogger();
        Account account = new Account(1001, "PORT-001");
        account.getPortfolio().addPosition(new Position("AAPL", 100, 150));
        account.getPortfolio().addPosition(new Position("MSFT", 50, 300));
        MarketDataCache.updatePrice(new MarketTick("AAPL", 150, 0, 0));
        MarketDataCache.updatePrice(new MarketTick("MSFT", 300, 0, 0));
        PortfolioValuationService valuation = new PortfolioValuationService(new ValuationService(), log);
        PortfolioManager manager = new PortfolioManager(valuation, new Account[]{account},
            new AlertService(log), new RiskService(log));
        WorkerNode[] workers = {new WorkerNode(0, manager), new WorkerNode(1, manager)};
        Dispatcher dispatcher = new Dispatcher(workers, new Partitioner(), new PartitionManager(workers.length));
        for (WorkerNode worker : workers) worker.start();
        try {
            dispatcher.dispatch(new MarketTick("AAPL", 160, 10, 1));
            dispatcher.dispatch(new MarketTick("MSFT", 320, 10, 2));
            dispatcher.dispatch(new MarketTick("AAPL", 165, 10, 3));
        } finally {
            RuntimeException failure = null;
            for (WorkerNode worker : workers) {
                try { worker.closeAndJoin(); }
                catch (RuntimeException e) { failure = e; }
            }
            if (failure != null) throw failure;
        }
        PortfolioValuationResult result = valuation.evaluatePortfolio(account.getPortfolio());
        System.out.printf("Final market value: %.2f%nUnrealized P&L: %.2f%n",
            result.getTotalMarketValue(), result.getTotalUnrealizedPnL());
    }
}
