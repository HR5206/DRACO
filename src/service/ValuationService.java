package src.service;

import src.model.Position;
import src.model.ValuationResult;

public class ValuationService {

    public double calculateMarketValue(
        Position position,
        double currentPrice) {
        return position.getQuantity() * currentPrice;
    }

    public double calculateCostBasis(Position position) {
        return position.getAveragePrice() * position.getQuantity();
    }

    public ValuationResult evaluate(Position position, double currentPrice) {
        double marketValue = calculateMarketValue(position, currentPrice);
        double costBasis = calculateCostBasis(position);
        double pnl = marketValue - costBasis;
        return new ValuationResult(marketValue, costBasis, pnl);
    }

}
