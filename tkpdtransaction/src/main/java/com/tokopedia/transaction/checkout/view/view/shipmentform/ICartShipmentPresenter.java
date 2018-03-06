package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICartShipmentPresenter {

    void processCheckout(CheckoutRequest checkoutRequest);
}
