package src.model;

public class Account {
    private int accountId;
    private Portfolio portfolio;
    public Account(int accountId, String portfolioId) {
        this.accountId = accountId;
        this.portfolio = new Portfolio(portfolioId);
    }
    public int getAccountId() {
        return accountId;
    }
    public Portfolio getPortfolio() {
        return portfolio;
    }
}
