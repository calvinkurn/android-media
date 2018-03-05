package com.tokopedia.transaction.checkout.view.view.shipmentform;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.checkout.domain.datamodel.cartcheckout.CheckoutData;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartShipmentActivity extends IBaseView {


    void renderCheckoutCartSuccess(CheckoutData checkoutData);

    void renderErrorCheckoutCart(String message);

    void renderErrorHttpCheckoutCart(String message);

    void renderErrorNoConnectionCheckoutCart(String message);

    void renderErrorTimeoutConnectionCheckoutCart(String message);


    void closeWithResult(int resultCode, @Nullable Intent intent);


}
