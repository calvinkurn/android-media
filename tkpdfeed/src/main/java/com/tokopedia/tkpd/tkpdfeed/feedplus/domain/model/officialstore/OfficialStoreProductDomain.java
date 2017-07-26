package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.officialstore;

import javax.annotation.Nullable;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreProductDomain {
    private final
    @Nullable
    Integer brand_id;

    private final
    @Nullable
    String brand_logo;

    private final
    @Nullable
    DataDomain data;

    public OfficialStoreProductDomain(Integer brand_id,
                                      String brand_logo,
                                      DataDomain data) {
        this.brand_id = brand_id;
        this.brand_logo = brand_logo;
        this.data = data;
    }

    @Nullable
    public Integer getBrand_id() {
        return brand_id;
    }

    @Nullable
    public String getBrand_logo() {
        return brand_logo;
    }

    @Nullable
    public DataDomain getData() {
        return data;
    }
}
