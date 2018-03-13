package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 05/03/18.
 */

public interface ICartShipmentPresenter {

    void processCheckout(CheckoutRequest checkoutRequest);

    void processVerifyPayment(String transactionId);

    void checkPromoShipment(Subscriber<CheckPromoCodeCartShipmentResult> subscriber,
                            CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest);

}
