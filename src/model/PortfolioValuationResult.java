package src.model;

import java.util.Map;

public class PortfolioValuationResult {
    private final double totalMarketValue;
    private final double totalCostBasis;
    private final double totalUnrealizedPnL;
    private final String portfolioId;
    private final Map<String, Double> positionMarketValues;

    public PortfolioValuationResult(double totalMarketValue, double totalCostBasis, double totalUnrealizedPnL, Map<String, Double> positionMarketValues, String portfolioId)
    {
        this.totalMarketValue = totalMarketValue;
        this.totalCostBasis = totalCostBasis;
        this.totalUnrealizedPnL = totalUnrealizedPnL;
        this.positionMarketValues = Map.copyOf(positionMarketValues);
        this.portfolioId = portfolioId;
    }

    public double getTotalMarketValue() {
        return totalMarketValue;
    }

    public double getTotalCostBasis() {
        return totalCostBasis;
    }

    public double getTotalUnrealizedPnL() {
        return totalUnrealizedPnL;
    }

    public Map<String, Double> getPositionMarketValues() {
        return positionMarketValues;
    }

    public String getPortfolioId() {
        return portfolioId;
    }
}
