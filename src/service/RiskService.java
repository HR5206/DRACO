package src.service;

import java.util.Collections;

import src.model.PortfolioValuationResult;
import src.model.RiskResult;
import src.model.AuditEvent;
import src.model.EventType;

public class RiskService 
{
    private final AuditLogger logger;

    public RiskService(AuditLogger logger) {
        this.logger = logger;
    }

    public RiskResult calculateRisk(PortfolioValuationResult valuationResult) {
        logger.log(new AuditEvent(EventType.RISK_CALCULATION, "Risk Calculation", System.currentTimeMillis(), valuationResult.getPortfolioId()));
        double exposure = valuationResult.getTotalMarketValue();
        double unrealizedPnL = valuationResult.getTotalUnrealizedPnL();
        double concentrationRisk = exposure == 0 || valuationResult.getPositionMarketValues().isEmpty()
            ? 0 : Collections.max(valuationResult.getPositionMarketValues().values()) / exposure;
        double exposureScore = exposure / 1_000_000.0;
        double lossScore = Math.max(0, -unrealizedPnL) / 100000.0;
        double overallScore = 0.5 * exposureScore + 0.3 * concentrationRisk + 0.2 * lossScore;
        return new RiskResult(exposure, concentrationRisk, unrealizedPnL, overallScore);
    }
}
