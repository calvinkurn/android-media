package com.tokopedia.transaction.cart.listener;

import android.content.Context;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.cartdata.CartCourierPrices;
import com.tokopedia.transaction.cart.model.cartdata.CartDonation;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartPromo;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartView extends IBaseView {
    void renderDepositInfo(String depositIdr);

    void renderTotalPaymentWithLoyalty(String grandTotalWithLPIDR);

    void renderTotalPaymentWithoutLoyalty(String grandTotalWithoutLPIDR);

    void renderButtonCheckVoucherListener();

    void renderVisiblePotentialCashBack(String cashBack);

    void renderInvisiblePotentialCashBack();

    void renderPaymentGatewayOption(List<GatewayList> gatewayList);

    void renderVisibleLoyaltyBalance(String loyaltyAmountIDR, String loyaltyPoint);

    void renderInvisibleLoyaltyBalance();

    void renderCartListData(String token, String ut, List<CartItem> cartList);

    void renderCheckoutCartDepositAmount(String depositAmount);

    void renderForceShowPaymentGatewaySelection();

    void renderVisibleErrorPaymentCart(
            @NonNull String messageError1, @NonNull String messageError2
    );

    void renderInvisibleErrorPaymentCart();

    void renderSuccessCheckVoucher(String voucherCode,
                                   String amount,
                                   String descVoucher,
                                   int instantVoucher);

    void renderErrorCheckVoucher(String message);

    void renderErrorFromInstantVoucher(int instantVoucher);

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

    void renderCheckboxDonasi(CartDonation donation);

    void setCheckoutCartToken(String token);

    List<CartItemEditable> getItemCartListCheckoutData();

    String getVoucherCodeCheckoutData();

    boolean isCheckoutDataUseVoucher();

    String getDonationValue();

    CheckoutData.Builder getCheckoutDataBuilder();

    String getDepositCheckoutData();

    void trackingCartCheckoutEvent();

    void trackingCartPayment();

    void trackingCartDepositEvent();

    void trackingCartDropShipperEvent();

    void trackingCartCancelEvent();

    LocalCacheHandler getLocalCacheHandlerNotificationData();

    void setCartSubTotal(CartCourierPrices cartCourierPrices);

    void setCartError(int cartIndex);

    void showRatesCompletion();

    void setCartNoGrandTotal();

    void refreshCartList();

    void renderInstantPromo(CartPromo cartPromo);

    void renderPromoView(boolean isCouponActive);

    Activity getActivity();

    void setListnerCancelPromoLayoutOnAutoApplyCode();

    Context getContext();
}
