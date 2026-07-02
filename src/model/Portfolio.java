package src.model;
import java.util.*;

public class Portfolio {
    
    private final String portfolioId;
    private Map<String, Position> positions;

    public Portfolio(String portfolioId) {
        this.portfolioId = portfolioId;
        positions = new HashMap<>();
    }

    public void addPosition(Position position) {
        positions.put(position.getSymbol(), position);
    }

    public Position getPosition(String symbol) {
        return positions.get(symbol);
    }

    public Collection<Position> getAllPositons() {
        return positions.values();
    }

    public String getPortfolioId() {
        return portfolioId;
    }
}
