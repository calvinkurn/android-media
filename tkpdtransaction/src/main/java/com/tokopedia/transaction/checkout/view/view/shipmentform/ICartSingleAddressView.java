package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;

import java.util.List;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public interface ICartSingleAddressView {


    void renderCheckPromoShipmentDataSuccess(CheckPromoCodeCartShipmentResult checkPromoCodeCartShipmentResult);

    void renderErrorCheckPromoShipmentData(String message);

    void renderErrorHttpCheckPromoShipmentData(String message);

    void renderErrorNoConnectionCheckPromoShipmentData(String message);

    void renderErrorTimeoutConnectionCheckPromoShipmentData(String message);
}
