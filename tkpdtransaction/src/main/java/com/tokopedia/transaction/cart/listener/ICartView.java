package com.tokopedia.transaction.cart.listener;

import com.tokopedia.core.product.listener.ViewListener;
import com.tokopedia.transaction.cart.model.cartdata.GatewayList;
import com.tokopedia.transaction.cart.model.cartdata.TransactionList;

import java.util.List;

/**
 * @author anggaprasetiyo on 11/3/16.
 */

public interface ICartView extends ViewListener {
    void renderDepositInfo(String depositIdr);

    void renderTotalPayment(String grandTotalWithoutLPIDR);

    void renderPaymentGatewayOption(List<GatewayList> gatewayList);

    void renderLoyaltyBalance(String lpAmountIdr, boolean visibleHolder);

    void renderCartListData(List<TransactionList> cartList);

    void renderCheckoutCartToken(String token);

    void renderCheckoutCartDepositAmount(String depositAmount);
}
