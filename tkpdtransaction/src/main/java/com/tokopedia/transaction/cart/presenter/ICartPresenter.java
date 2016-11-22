package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

import java.util.List;


/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartPresenter {
    void processGetCartData(@NonNull Activity activity);

    void processCancelCart(@NonNull Activity activity, @NonNull TransactionList data);

    void processCancelCartProduct(@NonNull Activity activity, @NonNull TransactionList cartData,
                                  @NonNull CartProduct cartProductData);

    void processSubmitEditCart(Activity activity, TransactionList cartData,
                               List<ProductEditData> cartProductEditDataList);

    void processUpdateInsurance(Activity activity, TransactionList cartData, boolean useInsurance);
}
