package com.tokopedia.transaction.cart.presenter;

import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
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

    void processCheckVoucherCode();

    void processGetTickerGTM();

    void processValidationCheckoutData();

    void unSubscribeObservable();

    void processValidationPayment(String paymentId);

    void processCheckoutAnalytics(LocalCacheHandler localCacheHandler, String gateway);

    void processPaymentAnalytics(LocalCacheHandler localCacheHandler, ThanksTopPayData data);

    void clearNotificationCart();
    void processCartRates(String token, List<CartItem> cartItemList);
}
