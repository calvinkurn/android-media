package com.tokopedia.transaction.checkout.domain.mapper;

import com.tokopedia.transaction.checkout.data.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.data.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transaction.checkout.data.entity.response.updatecart.UpdateCartDataResponse;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.DeleteCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.ResetCartData;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.UpdateCartData;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public interface ICartMapper {

    CartListData convertToCartItemDataList(CartDataListResponse cartDataListResponse);

    DeleteCartData convertToDeleteCartData(DeleteCartDataResponse deleteCartDataResponse);

    UpdateCartData convertToUpdateCartData(UpdateCartDataResponse updateCartDataResponse);

    ResetCartData convertToResetCartData(ResetCartDataResponse resetCartDataResponse);
}
