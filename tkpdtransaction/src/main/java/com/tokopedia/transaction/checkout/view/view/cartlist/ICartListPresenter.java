package com.tokopedia.transaction.checkout.view.view.cartlist;

import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;
import com.tokopedia.transaction.checkout.view.holderitemdata.CartItemHolderData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListPresenter {

    void processInitialGetCartData();

    void processDeleteCart(CartItemData cartItemData, boolean addWishList);

    void processDeleteAndRefreshCart(List<CartItemData> removedCartItems, boolean addWishList);

    void processToShipmentSingleAddress();

    void processToShipmentMultipleAddress(RecipientAddressModel selectedAddress);

    void reCalculateSubTotal(List<CartItemHolderData> dataList);

    void processCheckPromoCodeFromSuggestedPromo(String promoCode);

    void processToShipmentForm();

}
