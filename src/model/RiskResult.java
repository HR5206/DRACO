package src.model;

public class RiskResult {
    private final double exposure;
    private final double concentrationRisk;
    private final double unrealizedPnL;
    private final double overallScore;

    public RiskResult(double exposure, double concentrationRisk, double unrealizedPnL, double overallScore) 
    {
        this.exposure = exposure;
        this.concentrationRisk = concentrationRisk;
        this.unrealizedPnL = unrealizedPnL;
        this.overallScore = overallScore;
    }

    public double getExposure()
    {
        return exposure;
    }
    public double getConcentrationRisk()
    {
        return concentrationRisk;
    }
    public double getUnrealizedPnL()
    {
        return unrealizedPnL;
    }
    public double getOverallScore()
    {
        return overallScore;
    }
}
