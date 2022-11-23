package constants;

public class AppConstants {
    public static final String SELECT_PROVIDERS = "select * from (\n" +
            "SELECT * FROM\n" +
            "    (select * from (\n" +
            "SELECT * FROM providers as providers\n" +
            "LEFT JOIN\n" +
            "(select Min(price) as price2,sku as sku2 from providers group by sku) as sku\n" +
            "ON providers.sku = sku.sku2 and providers.price = sku.price2) as t where not price2 is null) as providers2\n" +
            "        LEFT JOIN\n" +
            "        (\n" +
            "select Min(rating) as rating2,sku as sku3 from\n" +
            "(select * from (\n" +
            "SELECT * FROM\n" +
            "    providers as providers\n" +
            "        LEFT JOIN\n" +
            "    (select Min(price) as price2,sku as sku2 from providers group by sku) as sku \n" +
            "    ON providers.sku = sku.sku2 and providers.price = sku.price2) as t where  not price2 is null) \n" +
            "    as l group by sku) as rating\n" +
            "    ON providers2.sku = rating.sku3 and providers2.rating = rating.rating2\n" +
            "    ) as u where  not sku3 is null ORDER BY sku;";
}
