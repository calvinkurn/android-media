package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.cartcheckout.CheckoutData;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartShipmentActivity extends IBaseView {

    void checkoutCart(CheckoutRequest checkoutRequest);

    void checkPromoCodeShipment(CheckPromoCodeCartShipmentRequest checkPromoCodeCartShipmentRequest);

    void renderCheckoutCartSuccess(CheckoutData checkoutData);

    void renderErrorCheckoutCart(String message);

    void renderErrorHttpCheckoutCart(String message);

    void renderErrorNoConnectionCheckoutCart(String message);

    void renderErrorTimeoutConnectionCheckoutCart(String message);


    void renderThanksTopPaySuccess(String message);

    void renderErrorThanksTopPaySuccess(String message);

    void renderErrorHttpThanksTopPaySuccess(String message);

    void renderErrorNoConnectionThanksTopPaySuccess(String message);

    void renderErrorTimeoutConnectionThanksTopPaySuccess(String message);


    void closeWithResult(int resultCode, @Nullable Intent intent);


}
