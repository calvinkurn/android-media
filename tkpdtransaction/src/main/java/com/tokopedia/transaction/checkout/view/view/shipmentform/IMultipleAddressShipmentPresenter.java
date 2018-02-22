package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.view.data.MultipleAddressPriceSummaryData;
import com.tokopedia.transaction.checkout.view.data.MultipleAddressShipmentAdapterData;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public interface IMultipleAddressShipmentPresenter {

    void sendData(List<MultipleAddressShipmentAdapterData> shipmentData,
                  MultipleAddressPriceSummaryData priceData);

}
