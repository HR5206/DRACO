package src.service;

import src.model.RiskResult;
import java.util.*;
import src.model.Alert;
import src.model.AlertType;
import src.model.AuditEvent;
import src.model.EventType;
import src.model.Portfolio;
import src.config.*;

public class AlertService {
    private final AuditLogger logger;

    public AlertService(AuditLogger logger) {
        this.logger = logger;
    }

    public List<Alert> generateAlerts(Portfolio portfolio, RiskResult risk) {
        List<Alert> alerts = new ArrayList<>();
        long timestamp = System.currentTimeMillis();
        String portfolioId = portfolio.getPortfolioId();
        if (risk.getExposure() > RiskConfiguration.EXPOSURE_LIMIT) {
            logger.log(new AuditEvent(EventType.RISK_CALCULATION, "Risk Calculation", System.currentTimeMillis(), null));
            alerts.add(new Alert(portfolioId, AlertType.HIGH_EXPOSURE, "Exposure exceeded configured threshold.", timestamp));
        }
        if (risk.getConcentrationRisk() > RiskConfiguration.CONCENTRATION_LIMIT) {
            alerts.add(new Alert(portfolioId, AlertType.HIGH_CONCENTRATION, "Portfolio concentration risk 70%", timestamp));
        }
        if (risk.getUnrealizedPnL() < RiskConfiguration.LOSS_LIMIT) {
            alerts.add(new Alert(portfolioId, AlertType.LARGE_UNREALIZED_LOSS, "Portfolio unrealized loss went below -1,00,000", timestamp));
        }
        return alerts;
    }
}
