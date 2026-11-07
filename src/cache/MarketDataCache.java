package src.cache;

import src.model.MarketTick;
import java.util.concurrent.*;;

public final class MarketDataCache {
    private static final ConcurrentHashMap<String, Double> latestPrices = new ConcurrentHashMap<>();
    private MarketDataCache() {}
    public static void updatePrice(MarketTick tick) {
        if (!Double.isFinite(tick.getPrice()) || tick.getPrice() <= 0) throw new IllegalArgumentException("Price must be finite and positive");
        latestPrices.put(tick.getSymbol(), tick.getPrice());
    }
    public static double getPrice(String symbol) {
        Double price = latestPrices.get(symbol);
        if (price == null) throw new IllegalStateException("No market price for " + symbol);
        return price;
    }
}
