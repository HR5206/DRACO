package src;

import src.config.RiskConfiguration;
import src.model.*;
import src.service.*;
import src.dispatcher.Dispatcher;
import src.distributed.Partitioner;
import src.distributed.WorkerNode;
import src.manager.PartitionManager;
import src.cache.MarketDataCache;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("DRACO Portfolio Risk Management System");
        System.out.println("========================================\n");

        try {
            System.out.println(">>> Setting up the system...\n");
            
            AuditLogger auditLogger = new AuditLogger();
            System.out.println("✓ Audit Logger initialized\n");

            System.out.println(">>> Creating sample accounts and portfolios...\n");
            
            Account account1 = new Account(1001, "PORT-001");
            Portfolio portfolio1 = account1.getPortfolio();
            
            portfolio1.addPosition(new Position("AAPL", 100, 150.00));
            System.out.println("✓ Account 1001: Added 100 AAPL @ $150.00 average price");
            
            portfolio1.addPosition(new Position("GOOG", 50, 350.00));
            System.out.println("✓ Account 1001: Added 50 GOOG @ $350.00 average price");
            
            portfolio1.addPosition(new Position("TSLA", 75, 500.00));
            System.out.println("✓ Account 1001: Added 75 TSLA @ $500.00 average price\n");

            Account account2 = new Account(1002, "PORT-002");
            Portfolio portfolio2 = account2.getPortfolio();
            
            portfolio2.addPosition(new Position("MSFT", 150, 300.00));
            System.out.println("✓ Account 1002: Added 150 MSFT @ $300.00 average price");
            
            portfolio2.addPosition(new Position("AAPL", 200, 145.00));
            System.out.println("✓ Account 1002: Added 200 AAPL @ $145.00 average price\n");

            Account[] allAccounts = {account1, account2};
            
            ValuationService valuationService = new ValuationService();
            PortfolioValuationService portfolioValuationService = 
                new PortfolioValuationService(valuationService, auditLogger);
            RiskService riskService = new RiskService(auditLogger);
            AlertService alertService = new AlertService(auditLogger);
            
            PortfolioManager portfolioManager = 
                new PortfolioManager(portfolioValuationService, allAccounts, alertService, riskService);
            System.out.println("✓ Portfolio Manager created with " + allAccounts.length + " accounts\n");

            WorkerNode[] workers = new WorkerNode[4];
            for (int i = 0; i < 4; i++) {
                workers[i] = new WorkerNode(i, portfolioManager);
            }
            System.out.println("✓ Created " + workers.length + " worker nodes\n");

            Partitioner partitioner = new Partitioner();
            PartitionManager partitionManager = new PartitionManager(workers.length);
            
            Dispatcher dispatcher = new Dispatcher(workers, partitioner, partitionManager);
            System.out.println("✓ Dispatcher configured for distributed processing\n");
            System.out.println("✓ Valuation, Risk, and Alert services initialized\n");

            System.out.println(">>> Initializing market data cache with starting prices...\n");
            MarketDataCache.updatePrice(new MarketTick("AAPL", 155.00, 0, System.currentTimeMillis()));
            MarketDataCache.updatePrice(new MarketTick("GOOG", 400.00, 0, System.currentTimeMillis()));
            MarketDataCache.updatePrice(new MarketTick("TSLA", 600.00, 0, System.currentTimeMillis()));
            MarketDataCache.updatePrice(new MarketTick("MSFT", 350.00, 0, System.currentTimeMillis()));
            System.out.println("✓ Market prices initialized\n");

            System.out.println(">>> Risk Configuration Limits:\n");
            System.out.println("  - Exposure Limit: $" + RiskConfiguration.EXPOSURE_LIMIT);
            System.out.println("  - Concentration Limit: " + (RiskConfiguration.CONCENTRATION_LIMIT * 100) + "%");
            System.out.println("  - Loss Limit: $" + RiskConfiguration.LOSS_LIMIT + "\n");

            System.out.println("========================================");
            System.out.println("PROCESSING MARKET TICKS");
            System.out.println("========================================\n");

            System.out.println(">>> Ticker 1: AAPL price moves to $160.00");
            MarketTick tick1 = new MarketTick("AAPL", 160.00, 1000, System.currentTimeMillis());
            MarketDataCache.updatePrice(tick1);
            dispatcher.dispatch(tick1);
            portfolioManager.processTick(tick1);
            System.out.println("");

            System.out.println(">>> Ticker 2: GOOG price moves to $420.00");
            MarketTick tick2 = new MarketTick("GOOG", 420.00, 2000, System.currentTimeMillis());
            MarketDataCache.updatePrice(tick2);
            dispatcher.dispatch(tick2);
            portfolioManager.processTick(tick2);
            System.out.println("");

            System.out.println(">>> Ticker 3: TSLA price moves to $650.00");
            MarketTick tick3 = new MarketTick("TSLA", 650.00, 1500, System.currentTimeMillis());
            MarketDataCache.updatePrice(tick3);
            dispatcher.dispatch(tick3);
            portfolioManager.processTick(tick3);
            System.out.println("");

            System.out.println(">>> Ticker 4: MSFT price moves to $380.00");
            MarketTick tick4 = new MarketTick("MSFT", 380.00, 1200, System.currentTimeMillis());
            MarketDataCache.updatePrice(tick4);

            System.out.println("========================================");
            System.out.println("DETAILED PORTFOLIO ANALYSIS");
            System.out.println("========================================\n");

            for (Account account : allAccounts) {
                System.out.println(">>> Account ID: " + account.getAccountId());
                Portfolio portfolio = account.getPortfolio();
                System.out.println("    Portfolio ID: " + portfolio.getPortfolioId());
                
                PortfolioValuationResult valuationResult = portfolioValuationService.evaluatePortfolio(portfolio);
                
                System.out.println("\n    Portfolio Valuation:");
                System.out.println("    - Total Market Value: $" + String.format("%.2f", valuationResult.getTotalMarketValue()));
                System.out.println("    - Total Cost Basis: $" + String.format("%.2f", valuationResult.getTotalCostBasis()));
                System.out.println("    - Total Unrealized P&L: $" + String.format("%.2f", valuationResult.getTotalUnrealizedPnL()));
                
                System.out.println("\n    Position Details:");
                for (Position pos : portfolio.getAllPositons()) {
                    double currentPrice = MarketDataCache.getPrice(pos.getSymbol());
                    double positionMarketValue = pos.getQuantity() * currentPrice;
                    double positionPnL = positionMarketValue - (pos.getQuantity() * pos.getAveragePrice());
                    
                    System.out.println("    - " + pos.getSymbol() + ": " + pos.getQuantity() + " shares");
                    System.out.println("      Cost: $" + String.format("%.2f", pos.getQuantity() * pos.getAveragePrice()) + 
                                     " | Market Value: $" + String.format("%.2f", positionMarketValue) + 
                                     " | P&L: $" + String.format("%.2f", positionPnL));
                }
                
                System.out.println("\n    Risk Analysis:");
                RiskResult riskResult = riskService.calculateRisk(valuationResult);
                System.out.println("    - Total Exposure: $" + String.format("%.2f", riskResult.getExposure()));
                System.out.println("    - Concentration Risk: " + String.format("%.2f", riskResult.getConcentrationRisk() * 100) + "%");
                System.out.println("    - Overall Risk Score: " + String.format("%.2f", riskResult.getOverallScore()));
                
                System.out.println("\n");
            }

            System.out.println("========================================");
            System.out.println("SYSTEM SUMMARY");
            System.out.println("========================================\n");
            System.out.println("✓ Successfully demonstrated:");
            System.out.println("  1. Account and Portfolio creation");
            System.out.println("  2. Position management");
            System.out.println("  3. Market data distribution via Dispatcher");
            System.out.println("  4. Portfolio valuation calculations");
            System.out.println("  5. Risk metric calculations");
            System.out.println("  6. Alert generation based on risk thresholds");
            System.out.println("  7. Audit event logging");
            System.out.println("  8. Distributed worker node routing\n");

            System.out.println("✓ System running successfully!");
            System.out.println("\nDRACO Portfolio Risk Management System Demo Complete");

        } catch (Exception e) {
            System.err.println("Error occurred during demo execution:");
            e.printStackTrace();
        }
    }
}
