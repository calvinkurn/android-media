package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

/**
 * Created by nakama on 09/03/18.
 */

public interface IMultipleAddressShipmentView {

    void showPromoMessage(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult,
                          CartItemPromoHolderData cartItemPromoHolderData);

    void showPromoError(String message);

}
