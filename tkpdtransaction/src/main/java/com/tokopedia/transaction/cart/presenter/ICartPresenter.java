package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;


/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartPresenter {
    void processGetCartData(@NonNull Activity activity);

    void processCancelCart(@NonNull Activity activity, @NonNull TransactionList data);

    void processCancelCartProduct(@NonNull Activity activity, @NonNull TransactionList cartData,
                                  @NonNull CartProduct cartProductData);
}
