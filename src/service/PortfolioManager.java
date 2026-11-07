package src.service;

import src.model.MarketTick;
import src.model.Portfolio;
import src.model.Alert;
import java.util.*;
import src.model.RiskResult;
import src.model.Position;
import src.model.PortfolioValuationResult;
import src.model.Account;

public class PortfolioManager{

    private final PortfolioValuationService valuationService;
    private final AlertService alertService;
    private final Account[] accounts;
    private final RiskService riskService;

    public PortfolioManager(PortfolioValuationService valuationService, Account[] accounts, AlertService alertService, RiskService riskService) {
        this.valuationService = valuationService;
        this.accounts = accounts;
        this.alertService = alertService;
        this.riskService = riskService;
    }
   
    public synchronized void processTick(MarketTick tick) {
        // Keep cache updates and the resulting valuation in one critical section.
        src.cache.MarketDataCache.updatePrice(tick);

        for (Account account : accounts) 
        {
            Portfolio portfolio = account.getPortfolio();
            Position findPosition = portfolio.getPosition(tick.getSymbol());
            if (findPosition != null) 
            {
                PortfolioValuationResult valuationResult = valuationService.evaluatePortfolio(portfolio);
                RiskResult risk = riskService.calculateRisk(valuationResult);
                List<Alert> alerts = alertService.generateAlerts(portfolio, risk);

                for (Alert alert : alerts) {
                    System.out.println(alert.getPortfolioId() + " | " + alert.getAlertType() + " | " + alert.getMessage());
                }
            }
        }
    }
}