package tests;
import src.cache.MarketDataCache;
import src.model.*;
import src.service.*;
import src.distributed.*;
import java.util.Map;
public class RegressionTest {
    public static void main(String[] args) throws Exception {
        AuditLogger log = new AuditLogger();
        Account a = new Account(1, "test");
        a.getPortfolio().addPosition(new Position("TEST", 2, 10));
        PortfolioValuationService v = new PortfolioValuationService(new ValuationService(), log);
        WorkerNode w = new WorkerNode(0, new PortfolioManager(v, new Account[]{a}, new AlertService(log), new RiskService(log)));
        w.start();
        for (int i = 1; i <= 50; i++) w.addTick(new MarketTick("TEST", i, 0, i));
        w.closeAndJoin();
        if (w.isAlive() || MarketDataCache.getPrice("TEST") != 50) throw new AssertionError("Drain/FIFO failed");
        if (v.evaluatePortfolio(a.getPortfolio()).getTotalUnrealizedPnL() != 80) throw new AssertionError("P&L failed");
        if (new RiskService(log).calculateRisk(new PortfolioValuationResult(0,0,0,Map.of(),"empty")).getConcentrationRisk() != 0) throw new AssertionError("Empty risk failed");
        try { MarketDataCache.getPrice("MISSING"); throw new AssertionError("Missing price accepted"); } catch (IllegalStateException expected) {}
        try { w.addTick(new MarketTick("TEST",1,0,0)); throw new AssertionError("Closed worker accepted tick"); } catch (IllegalStateException expected) {}
        System.out.println("DRACO regression checks passed");
    }
}
