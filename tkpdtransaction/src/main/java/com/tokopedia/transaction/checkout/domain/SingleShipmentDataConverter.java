package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;

import java.util.List;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 08/02/18.
 */

public class SingleShipmentDataConverter extends ConverterData<List<CartItemData>, CartSingleAddressData> {

    @Inject
    public SingleShipmentDataConverter() {
    }

    @Override
    public CartSingleAddressData convert(List<CartItemData> originData) {
        return null;
    }
}
