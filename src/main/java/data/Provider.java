package data;

public class Provider {
    public String provider;
    public String vendorCode;
    public String brand;
    public String sku;
    public double count;
    public double minCount;
    public double price;
    public double maxPrice;
    public double minPrice;
    public double rating;

    public Provider(
            String provider,
            String vendorCode,
            String brand,
            String sku,
            double count,
            double minCount,
            double price,
            double maxPrice,
            double minPrice,
            double rating
    ) {
        this.provider = provider;
        this.vendorCode = vendorCode;
        this.brand = brand;
        this.sku = sku;
        this.count = count;
        this.minCount = minCount;
        this.price = price;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.rating = rating;
    }

    public String getProvider () {
        return
                "################" +
                this.provider + ";" +
                this.vendorCode + ";" +
                this.brand + ";" +
                this.sku + ";" +
                this.count + ";" +
                this.minCount + ";" +
                this.price + ";" +
                this.maxPrice + ";" +
                this.minPrice + ";" +
                this.rating + ";";
    }
}
