package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ShippingDomain {
    private int id;
    @Nullable
    private String name;
    @Nullable
    private ShippingDetailDomain detailDomain;

    public ShippingDomain(int id,
                          @Nullable String name,
                          @Nullable ShippingDetailDomain detailDomain) {
        this.id = id;
        this.name = name;
        this.detailDomain = detailDomain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public ShippingDetailDomain getDetailDomain() {
        return detailDomain;
    }

    public void setDetailDomain(@Nullable ShippingDetailDomain detailDomain) {
        this.detailDomain = detailDomain;
    }
}
