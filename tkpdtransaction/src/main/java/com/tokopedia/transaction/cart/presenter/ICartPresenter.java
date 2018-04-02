package com.tokopedia.transaction.cart.presenter;

import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;

import java.util.List;


/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartPresenter {
    void processGetCartData();

    void processCancelCart(@NonNull CartItem cartData);

    void processCancelCartProduct(@NonNull CartItem cartData,
                                  @NonNull CartProduct cartProductData);

    void processSubmitEditCart(@NonNull CartItem cartData,
                               @NonNull List<ProductEditData> cartProductEditDataList);

    void processUpdateInsurance(@NonNull CartItemEditable cartItemEditable, boolean useInsurance);

    void processCheckVoucherCode(String voucherCode, int instantVoucher);

    void processGetTickerGTM();

    void processValidationCheckoutData();

    void unSubscribeObservable();

    void processCartRates(String token, String ut, List<CartItem> cartItemList);

    void processValidationPayment(String paymentId);

    void trackStep1CheckoutEE(Checkout checkoutData);

    void trackStep2CheckoutEE(Checkout checkoutData);

    void processPaymentAnalytics(LocalCacheHandler localCacheHandler, ThanksTopPayData data);

    void cancelPromo();

    void clearNotificationCart();
}
