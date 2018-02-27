package com.tokopedia.transaction.checkout.view.view.cartlist;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.RecipientAddressModel;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void processInitialGetCartData();

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processToShipmentSingleAddress();

    void processToShipmentMultipleAddress(RecipientAddressModel selectedAddress);

    void reCalculateSubTotal(List<CartItemHolderData> dataList);
}
