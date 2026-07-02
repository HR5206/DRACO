package src.model;

public class AuditEvent {
    private final EventType type;
    private final String description;
    private final long timestamp;
    private final String portfolioId;

    public AuditEvent(EventType type, String description, long timestamp, String portfolioId)
    {
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
        this.portfolioId = portfolioId;
    }

    public EventType getEventType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPortfolioId()
    {
        return portfolioId;
    }
}
