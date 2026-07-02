package src.model;

public class Alert {
    private final String portfolioId;
    private final AlertType type;
    private final String message;
    private final long timestamp;

    public Alert(String portfolioId, AlertType type, String message, long timestamp) {
        this.portfolioId = portfolioId;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public AlertType getAlertType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
