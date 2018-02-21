package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.view.data.ShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public interface IShipmentMapper {

    ShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    );
}
