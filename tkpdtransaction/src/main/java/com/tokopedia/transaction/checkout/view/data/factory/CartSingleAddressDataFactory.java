package com.tokopedia.transaction.checkout.view.data.factory;

import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;

/**
 * @author Aghny A. Putra on 31/01/18
 */

public class CartSingleAddressDataFactory {

    public static CartSingleAddressData getDummyCartSingleAddressData() {
        return createDummyCartSingleAddressData();
    }

    private static CartSingleAddressData createDummyCartSingleAddressData() {
        CartSingleAddressData cartSingleAddressData = new CartSingleAddressData();

        cartSingleAddressData.setShipmentFeeBannerModel(ShipmentFeeBannerModelFactory.getDummyShippingFeeBannerModel());

        return cartSingleAddressData;
    }

}
