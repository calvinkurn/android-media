package com.tokopedia.seller.opportunity.viewmodel;

/**
 * Created by nisie on 3/6/17.
 */

public class ShippingTypeViewModel {
    int shippingTypeId;
    String shippingTypeName;

    public ShippingTypeViewModel(String shippingTypeName, int shippingTypeId) {
        this.shippingTypeName = shippingTypeName;
        this.shippingTypeId = shippingTypeId;
    }
}
