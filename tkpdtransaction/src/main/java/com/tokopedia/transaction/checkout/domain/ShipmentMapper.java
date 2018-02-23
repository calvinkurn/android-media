package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.CartShipmentAddressFormData;

/**
 * @author anggaprasetiyo on 21/02/18.
 */

public class ShipmentMapper implements IShipmentMapper {
    @Override
    public CartShipmentAddressFormData convertToShipmentAddressFormData(
            ShipmentAddressFormDataResponse shipmentAddressFormDataResponse
    ) {
        CartShipmentAddressFormData data = new CartShipmentAddressFormData();
        return data;
    }
}
