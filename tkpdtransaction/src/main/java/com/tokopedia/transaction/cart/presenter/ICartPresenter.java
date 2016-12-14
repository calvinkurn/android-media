package com.tokopedia.transaction.cart.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.calculateshipment.ProductEditData;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

import java.util.List;


/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartPresenter {
    void processGetCartData();

    void processCancelCart(@NonNull TransactionList cartData);

    void processCancelCartProduct(@NonNull TransactionList cartData,
                                  @NonNull CartProduct cartProductData);

    void processSubmitEditCart(@NonNull TransactionList cartData,
                               @NonNull List<ProductEditData> cartProductEditDataList);

    void processUpdateInsurance(@NonNull TransactionList cartData, boolean useInsurance);

    void processCheckVoucherCode();

    void processGetTickerGTM();

    void processValidationCheckoutData();
}
