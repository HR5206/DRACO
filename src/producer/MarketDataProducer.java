package src.producer;

import src.dispatcher.Dispatcher;
import src.model.MarketTick;
import src.service.AuditLogger;
import src.model.AuditEvent;
import src.model.EventType;

import java.util.*;
import src.cache.*;

public class MarketDataProducer extends Thread{
    private final String[] symbols = {
        "AAPL",
        "GOOG",
        "TSLA",
        "MSFT"
    };

    private final Random random = new Random();

    private final Map<String, Double> currentPrices = new HashMap<>();;

    private final Dispatcher dispatcher;

    private final AuditLogger logger;

    private void initializePrices()
    {
        currentPrices.put("AAPL", 200.00);
        currentPrices.put("GOOG", 400.00);
        currentPrices.put("TSLA", 600.00);
        currentPrices.put("MSFT", 800.00);
    }

    public MarketDataProducer(Dispatcher dispatcher, AuditLogger logger)
    {
        this.dispatcher = dispatcher;
        this.logger = logger;
    }

    @Override
    public void run()
    {
        initializePrices();
        while (true)
        {
            String symbol = symbols[random.nextInt(symbols.length)];
            double currentPrice = currentPrices.get(symbol);
            double updatedPrice = currentPrice + random.nextDouble(-2.0, 2.0);
            updatedPrice = Math.max(1, updatedPrice);
            currentPrices.put(symbol, updatedPrice);
            double price = updatedPrice;
            int volume = 10 + random.nextInt(500);

            MarketTick tick = new MarketTick(symbol, price, volume, System.currentTimeMillis());

            logger.log(new AuditEvent(EventType.MARKET_TICK, "Market Tick: " + symbol, System.currentTimeMillis(), ""));

            try {
                dispatcher.dispatch(tick);
                MarketDataCache.updatePrice(tick);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}
