package com.tokopedia.transaction.cart.listener;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartView extends ViewListener {
    void renderDepositInfo(String depositIdr);

    void renderTotalPayment(String grandTotalWithoutLPIDR);

    void renderButtonCheckVoucherListener();

    void renderVisiblePotentialCashBack(String cashBack);

    void renderInvisiblePotentialCashBack();

    void renderPaymentGatewayOption(List<GatewayList> gatewayList);

    void renderVisibleLoyaltyBalance(String loyaltyAmountIDR);

    void renderInvisibleLoyaltyBalance();

    void renderCartListData(List<CartItem> cartList);

    void renderCheckoutCartDepositAmount(String depositAmount);

    void renderVisibleErrorPaymentCart(@NonNull String messageError);

    void renderInvisibleErrorPaymentCart();

    void renderSuccessCheckVoucher(String descVoucher);

    void renderErrorCheckVoucher(String message);

    void renderErrorEmptyCart();

    void renderVisibleMainCartContainer();

    void renderInitialLoadingCartInfo();

    void renderVisibleTickerGTM(String message);

    void renderInvisibleTickerGTM();

    void renderDisableErrorCheckVoucher();

    void renderErrorCartItem(CartItemEditable cartItemEditable);

    void renderErrorTimeoutInitialCartInfo(String messageError);

    void renderErrorNoConnectionInitialCartInfo(String messageError);

    void renderErrorResponseInitialCartInfo(String messageError);

    void renderErrorDefaultInitialCartInfo(String messageError);

    void executeService(Intent intent);

    void setCheckoutCartToken(String token);

    List<CartItemEditable> getItemCartListCheckoutData();

    String getVoucherCodeCheckoutData();

    boolean isCheckoutDataUseVoucher();

    CheckoutData.Builder getCheckoutDataBuilder();

    String getDepositCheckoutData();

    String getStringFromResource(@StringRes int resId);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );

    Activity getContextActivity();


}
