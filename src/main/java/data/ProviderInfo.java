package data;

public class ProviderInfo {
    public int priority;
    public String provider;
    public String fileName;
    public double priceFromParam;
    public double priceToParam;
    public double maxPrice;
    public double minPrice;

    public ProviderInfo(
            int priority,
            String provider,
            String fileName,
            double priceFromParam,
            double priceToParam,
            double maxPrice,
            double minPrice
    ) {
        this.priority = priority;
        this.provider = provider;
        this.fileName = fileName;
        this.priceFromParam = priceFromParam;
        this.priceToParam = priceToParam;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }
}
