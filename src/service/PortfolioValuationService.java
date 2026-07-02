package src.service;

import java.util.*;
import src.model.Position;
import src.model.ValuationResult;
import src.cache.MarketDataCache;
import src.model.AuditEvent;
import src.model.EventType;
import src.model.Portfolio;
import src.model.PortfolioValuationResult;

public class PortfolioValuationService {
    private final ValuationService valuationService;
    private final AuditLogger logger;

    public PortfolioValuationService(ValuationService valuationService, AuditLogger logger) {
        this.valuationService = valuationService;
        this.logger = logger;
    }

    public PortfolioValuationResult evaluatePortfolio(Portfolio portfolio) 
    {
        logger.log(new AuditEvent(EventType.PORTFOLIO_VALUATION, "Portfolio Valuation for " + portfolio.getPortfolioId(), System.currentTimeMillis(), portfolio.getPortfolioId()));

        Collection<Position> positions = portfolio.getAllPositons();
        
        double totalMarketValue = 0;
        double totalCostBasis = 0;
        double totalUnrealizedPnL = 0;
        Map<String, Double> positionMarketValues = new HashMap<>();

        for (Position position : positions) {
            ValuationResult result = valuationService.evaluate(position, MarketDataCache.getPrice(position.getSymbol()));
            double currentMarketValue = result.getMarketValue();
            double currentCostBasis = result.getCostBasis();
            totalMarketValue +=currentMarketValue;
            totalCostBasis += currentCostBasis;
            totalUnrealizedPnL += result.getUnrealizedPnL();
            positionMarketValues.put(position.getSymbol(), currentMarketValue);
        }

        return new PortfolioValuationResult(totalMarketValue, totalCostBasis, totalUnrealizedPnL, positionMarketValues, portfolio.getPortfolioId());
    }
}
