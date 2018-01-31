package com.tokopedia.transaction.checkout.view.activity;

import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartShipmentActivity {

    List<CartItemData> getCartItemDataList();

    void goToSingleAddressCart(Object data);

    void goToMultipleAddressCart(Object data);
}
