package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.payment.utils.ErrorNetMessage;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter {

    private final ICartSingleAddressView view;

    @Inject
    public SingleAddressShipmentPresenter(ICartSingleAddressView view) {
        this.view = view;
    }

    Subscriber<CheckPromoCodeCartShipmentResult> getSubscriberCheckPromoShipment() {
        return new Subscriber<CheckPromoCodeCartShipmentResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                view.renderErrorCheckPromoShipmentData(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }

            @Override
            public void onNext(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult) {
                if (!checkPromoCodeCartShipmentResult.isError()) {
                    view.renderCheckPromoShipmentDataSuccess(checkPromoCodeCartShipmentResult);
                } else {
                    view.renderErrorCheckPromoShipmentData(checkPromoCodeCartShipmentResult.getErrorMessage());
                }
            }
        };
    }
}