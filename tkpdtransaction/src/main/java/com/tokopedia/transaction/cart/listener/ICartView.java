package com.tokopedia.transaction.cart.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.cart.model.CartItemEditable;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;
import com.tokopedia.transaction.cart.model.paramcheckout.CheckoutData;
import com.tokopedia.transaction.cart.model.voucher.VoucherData;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartView extends ViewListener {
    void renderDepositInfo(String depositIdr);

    void renderTotalPayment(String grandTotalWithoutLPIDR);

    void renderButtonCheckVoucherListener();

    void renderVisiblePotentialCashBack(String cashBack);

    void renderGonePotentialCashBack();

    void renderPaymentGatewayOption(List<GatewayList> gatewayList);

    void renderLoyaltyBalance(String lpAmountIdr, boolean visibleHolder);

    void renderCartListData(List<TransactionList> cartList);

    void renderCheckoutCartToken(String token);

    void renderCheckoutCartDepositAmount(String depositAmount);

    void renderErrorPaymentCart(boolean isError, @NonNull String messageError);

    void renderSuccessVoucherChecked(String messageSuccess, VoucherData data);

    void showAlertDialogInfo(String messageSuccess);

    void renderErrorCheckVoucher(String message);

    void renderEmptyCart();

    void renderVisibleMainCartContainer();

    void renderInitialLoadingCartInfo();

    void renderVisibleTickerGTM(String message);

    void renderGoneTickerGTM();

    List<CartItemEditable> getItemCartListCheckoutData();

    String getVoucherCodeCheckoutData();

    boolean isCheckoutDataUseVoucher();

    void renderDisableErrorCheckVoucher();

    CheckoutData.Builder getCheckoutDataBuilder();

    String getDepositCheckoutData();

    void renderErrorCartItem(CartItemEditable cartItemEditable);
}
