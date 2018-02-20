package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void processGetCartData();

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processToShipmentStep();

    void reCalculateSubTotal(List<CartItemHolderData> dataList);

    void processUpdateCart();
}
