package src.model;

public class MarketTick{
    
    private String symbol;
    private double price;
    private int volume;
    private long timestamp;

    public MarketTick(String symbol, double price, int volume, long timestamp)
    {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    @Override 
    public String toString()
    {
        return symbol + " Price=" + price + " Volume=" + volume + " Time=" + timestamp;
    }
}
