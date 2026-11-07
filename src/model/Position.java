package src.model;

public class Position {
    private String symbol;
    private int quantity;
    private double averagePrice;

    public Position(String symbol, int quantity, double averagePrice) {
        this.symbol = symbol;
        if (quantity < 0) throw new IllegalArgumentException("Only long positions are supported");
        this.quantity = quantity;
        if (!Double.isFinite(averagePrice) || averagePrice < 0) throw new IllegalArgumentException("Invalid average price");
        this.averagePrice = averagePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Only long positions are supported");
        this.quantity = quantity;
    }

    public void setAveragePrice(double averagePrice) {
        if (!Double.isFinite(averagePrice) || averagePrice < 0) throw new IllegalArgumentException("Invalid average price");
        this.averagePrice = averagePrice;
    }
}