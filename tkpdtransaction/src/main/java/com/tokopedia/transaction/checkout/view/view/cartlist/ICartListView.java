package com.tokopedia.transaction.checkout.view.view.cartlist;

import com.tokopedia.transaction.base.IBaseView;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.CartPromoSuggestion;
import com.tokopedia.transaction.checkout.view.data.cartshipmentform.CartShipmentAddressFormData;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public interface ICartListView extends IBaseView {

    void renderInitialGetCartListDataSuccess(CartListData cartListData);

    void renderErrorInitialGetCartListData(String message);

    void renderErrorHttpInitialGetCartListData(String message);

    void renderErrorNoConnectionInitialGetCartListData(String message);

    void renderErrorTimeoutConnectionInitialGetCartListData(String message);


    void renderActionDeleteCartDataSuccess(CartItemData cartItemData, String message, boolean addWishList);

    void renderErrorActionDeleteCartData(String message);

    void renderErrorHttpActionDeleteCartData(String message);

    void renderErrorNoConnectionActionDeleteCartData(String message);

    void renderErrorTimeoutConnectionActionDeleteCartData(String message);


    void renderToShipmentSingleAddressSuccess(CartShipmentAddressFormData shipmentAddressFormData);

    void renderErrorToShipmentSingleAddress(String message);

    void renderErrorHttpToShipmentSingleAddress(String message);

    void renderErrorNoConnectionToShipmentSingleAddress(String message);

    void renderErrorTimeoutConnectionToShipmentSingleAddress(String message);


    void renderToShipmentMultipleAddressSuccess(CartListData cartListData);

    void renderErrorToShipmentMultipleAddress(String message);

    void renderErrorHttpToShipmentMultipleAddress(String message);

    void renderErrorNoConnectionToShipmentMultipleAddress(String message);

    void renderErrorTimeoutConnectionToShipmentMultipleAddress(String message);


    void renderEmptyCartData();

    void disableSwipeRefresh();

    void enableSwipeRefresh();

    List<CartItemData> getCartDataList();

    void renderDetailInfoSubTotal(String qty, String subtotalPrice);

    void renderPromoVoucher();

    void showToastMessageRed(String message);

    void renderLoadGetCartData();

    void renderLoadGetCartDataFinish();

}
