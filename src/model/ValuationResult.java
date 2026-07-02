package src.model;

public class ValuationResult {

    private double marketValue;
    private double costBasis;
    private double unrealizedPnL;

    public ValuationResult(double marketValue, double costBasis, double unrealizedPnL) {
        this.marketValue = marketValue;
        this.costBasis = costBasis;
        this.unrealizedPnL = unrealizedPnL;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getCostBasis() {
        return costBasis;
    }

    public double getUnrealizedPnL() {
        return unrealizedPnL;
    }
}
