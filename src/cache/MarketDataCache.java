package src.cache;

import src.model.MarketTick;
import java.util.concurrent.*;;

public final class MarketDataCache {
    private static final ConcurrentHashMap<String, Double> latestPrices = new ConcurrentHashMap<>();
    private MarketDataCache() {}
    public static void updatePrice(MarketTick tick) {
        latestPrices.put(tick.getSymbol(), tick.getPrice());
    }
    public static double getPrice(String symbol) {
        return latestPrices.getOrDefault(symbol, 0.0);
    }
}
