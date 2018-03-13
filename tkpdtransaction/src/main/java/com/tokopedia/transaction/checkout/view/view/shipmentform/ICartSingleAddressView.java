package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeCartListData;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ICartSingleAddressView extends IBaseView {

    void showLoading();

    void hideLoading();

    void renderCheckPromoShipmentDataSuccess(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult);

    void renderErrorCheckPromoShipmentData(String message);

    void renderErrorHttpCheckPromoShipmentData(String message);

    void renderErrorNoConnectionCheckPromoShipmentData(String message);

    void renderErrorTimeoutConnectionCheckPromoShipmentData(String message);


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
