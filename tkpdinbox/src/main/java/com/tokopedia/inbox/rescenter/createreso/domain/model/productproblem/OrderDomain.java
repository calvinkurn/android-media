package com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem;

import javax.annotation.Nullable;

/**
 * Created by yoasfs on 16/08/17.
 */

public class OrderDomain {
    @Nullable
    private OrderDetailDomain detailDomain;
    @Nullable
    private OrderProductDomain productDomain;
    @Nullable
    private ShippingDomain shippingDomain;

    public OrderDomain(@Nullable OrderDetailDomain detailDomain,
                       @Nullable OrderProductDomain productDomain,
                       @Nullable ShippingDomain shippingDomain) {
        this.detailDomain = detailDomain;
        this.productDomain = productDomain;
        this.shippingDomain = shippingDomain;
    }

    @Nullable
    public OrderDetailDomain getDetailDomain() {
        return detailDomain;
    }

    public void setDetailDomain(@Nullable OrderDetailDomain detailDomain) {
        this.detailDomain = detailDomain;
    }

    @Nullable
    public OrderProductDomain getProductDomain() {
        return productDomain;
    }

    public void setProductDomain(@Nullable OrderProductDomain productDomain) {
        this.productDomain = productDomain;
    }

    @Nullable
    public ShippingDomain getShippingDomain() {
        return shippingDomain;
    }

    public void setShippingDomain(@Nullable ShippingDomain shippingDomain) {
        this.shippingDomain = shippingDomain;
    }
}
