package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.ShippingFeeBannerModel;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class ShippingFeeBannerModelFactory {

    public static ShippingFeeBannerModel getDummyShippingFeeBannerModel() {
        return createShippingFeeBannerModel(true, "Rp35.000");
    }

    public static ShippingFeeBannerModel createShippingFeeBannerModel(boolean visible,
                                                                      String shipmentFeeDiscount) {
        ShippingFeeBannerModel shippingFeeBannerModel = new ShippingFeeBannerModel();
        shippingFeeBannerModel.setVisible(visible);
        shippingFeeBannerModel.setShipmentFeeDiscount(shipmentFeeDiscount);

        return shippingFeeBannerModel;
    }

}
