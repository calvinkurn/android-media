package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.ShipmentFeeBannerModel;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class ShipmentFeeBannerModelFactory {

    public static ShipmentFeeBannerModel getDummyShippingFeeBannerModel() {
        return createShippingFeeBannerModel(true, "Rp35.000");
    }

    public static ShipmentFeeBannerModel createShippingFeeBannerModel(boolean visible,
                                                                      String shipmentFeeDiscount) {
        ShipmentFeeBannerModel shipmentFeeBannerModel = new ShipmentFeeBannerModel();
        shipmentFeeBannerModel.setVisible(visible);
        shipmentFeeBannerModel.setShipmentFeeDiscount(shipmentFeeDiscount);

        return shipmentFeeBannerModel;
    }

}
