package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentResult;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;
import com.tokopedia.transaction.checkout.domain.datamodel.voucher.PromoCodeAppliedData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemPromoHolderData;

import java.util.List;

import rx.Subscriber;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressShipmentPresenter {

    CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                            MultipleAddressPriceSummaryData priceData, String promoCode);

    List<MultipleAddressShipmentAdapterData> initiateAdapterData(
            CartShipmentAddressFormData dataFromWebService
    );

    CheckPromoCodeCartShipmentRequest generateCheckPromoRequest(
            List<MultipleAddressShipmentAdapterData> shipmentData, CartItemPromoHolderData appliedPromo
    );

    CartItemPromoHolderData generateCartPromoHolderData(PromoCodeAppliedData appliedPromoData);

    Subscriber<CheckPromoCodeCartShipmentResult> checkPromoSubscription(
            CartItemPromoHolderData cartItemPromoHolderData);

    void processCheckShipmentFormPrepareCheckout();

    void processCheckPromoCodeFromSuggestedPromo(String promoCode);
}
