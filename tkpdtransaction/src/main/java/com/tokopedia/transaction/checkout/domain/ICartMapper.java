package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;

import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(CartDataListResponse cartDataListResponse);

    DeleteCartData convertToDeleteCartData(DeleteCartDataResponse deleteCartDataResponse);
}
