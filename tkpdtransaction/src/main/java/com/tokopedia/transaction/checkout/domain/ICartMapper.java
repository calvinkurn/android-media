package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    List<CartItemData> convertToCartItemDataList(CartDataListResponse cartDataListResponse);
}
