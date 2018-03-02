package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.domain.datamodel.MultipleAddressShipmentAdapterData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartshipmentform.CartShipmentAddressFormData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressShipmentPresenter {

    void sendData(List<MultipleAddressShipmentAdapterData> shipmentData,
                  MultipleAddressPriceSummaryData priceData);

    List<MultipleAddressShipmentAdapterData> setAdapter(
            CartShipmentAddressFormData dataFromWebService
    );
}
