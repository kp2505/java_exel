package data;

import java.util.Locale;

public class Sku {
    public String id;
    public String value;

    public Sku(
            String id,
            String value
    ) {
        this.id = id;
        this.value = value;
    }

    public String getCompareValue() {
        return id.toLowerCase(Locale.ROOT).trim();
    }
}
