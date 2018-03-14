package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

/**
 * Created by nakama on 09/03/18.
 */

public interface IMultipleAddressShipmentView {

    void showLoading();

    void hideLoading();

    void showPromoMessage(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult,
                          CartItemPromoHolderData cartItemPromoHolderData);

    void showPromoError(String message);

    void renderCheckShipmentPrepareCheckoutSuccess();

    void renderErrorDataHasChangedCheckShipmentPrepareCheckout(CartShipmentAddressFormData cartShipmentAddressFormData);

    void renderErrorCheckShipmentPrepareCheckout(String message);

    void renderErrorHttpCheckShipmentPrepareCheckout(String message);

    void renderErrorNoConnectionCheckShipmentPrepareCheckout(String message);

    void renderErrorTimeoutConnectionCheckShipmentPrepareCheckout(String message);

    TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams
    );

    void renderCheckPromoCodeFromSuggestedPromoSuccess(PromoCodeCartListData promoCodeCartListData);

    void renderErrorCheckPromoCodeFromSuggestedPromo(String message);
}
