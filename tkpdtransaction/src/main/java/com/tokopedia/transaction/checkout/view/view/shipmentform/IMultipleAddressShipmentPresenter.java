package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.core.router.transactionmodule.sharedata.CheckPromoCodeCartShipmentRequest;
import com.tokopedia.transaction.checkout.data.entity.request.CheckoutRequest;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressShipmentPresenter {

    CheckoutRequest generateCheckoutRequest(List<MultipleAddressShipmentAdapterData> shipmentData,
                                            MultipleAddressPriceSummaryData priceData);

    List<MultipleAddressShipmentAdapterData> initiateAdapterData(
            CartShipmentAddressFormData dataFromWebService
    );

    CheckPromoCodeCartShipmentRequest generateCheckPromoRequest(
            List<MultipleAddressShipmentAdapterData> shipmentData
    );
}
