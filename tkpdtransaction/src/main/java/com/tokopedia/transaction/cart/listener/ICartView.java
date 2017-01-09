package com.tokopedia.transaction.cart.listener;

import android.support.annotation.NonNull;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartView extends IBaseView {
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

    void renderForceShowPaymentGatewaySelection();

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

    void setCheckoutCartToken(String token);

    List<CartItemEditable> getItemCartListCheckoutData();

    String getVoucherCodeCheckoutData();

    boolean isCheckoutDataUseVoucher();

    CheckoutData.Builder getCheckoutDataBuilder();

    String getDepositCheckoutData();

    void trackingCartCheckoutEvent();

    void trackingCartPayment();

    void trackingCartDepositEvent();

    void trackingCartDropShipperEvent();

    void trackingCartCancelEvent();
}
