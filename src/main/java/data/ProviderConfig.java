package data;

import java.util.List;

public class ProviderConfig {
    public List<String> forbiddenBrands;
    public List<ProviderInfo> providerInfoList;
    public List<Sku> existedSkuList;

    public ProviderConfig(
            List<String> forbiddenBrands,
            List<ProviderInfo> providerInfoList,
            List<Sku> existedSkuList
    ) {
        this.forbiddenBrands = forbiddenBrands;
        this.providerInfoList = providerInfoList;
        this.existedSkuList = existedSkuList;
    }
}
