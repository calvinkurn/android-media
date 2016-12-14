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
    void processGetCartData(@NonNull Activity activity);

    void processCancelCart(@NonNull Activity activity, @NonNull TransactionList data);

    void processCancelCartProduct(@NonNull Activity activity, @NonNull TransactionList cartData,
                                  @NonNull CartProduct cartProductData);

    void processSubmitEditCart(@NonNull Activity activity, @NonNull TransactionList cartData,
                               @NonNull List<ProductEditData> cartProductEditDataList);

    void processUpdateInsurance(@NonNull Activity activity, @NonNull TransactionList cartData,
                                boolean useInsurance);

    void processCheckoutCart(
            @NonNull Activity activity, @NonNull CheckoutData.Builder checkoutDataBuilder,
            @NonNull List<CartItemEditable> cartItemEditables
    );

    void processCheckVoucherCode(@NonNull Activity activity, @NonNull String voucherCode);

    void processStep2PaymentCart(Activity activity, TopPayParameterData data);

    void processGetTickerGTM();

    void processValidationCheckoutData(Activity activity);
}
