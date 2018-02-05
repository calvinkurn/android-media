package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoMenuData;
import com.tokopedia.transaction.checkout.domain.response.cartlist.CartDataListResponse;
import com.tokopedia.transaction.checkout.domain.response.deletecart.DeleteCartDataResponse;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartRepository {

    Observable<CartDataListResponse> getCartList(TKPDMapParam<String, String> param);

    Observable<DeleteCartDataResponse> deleteCartData(JsonObject param);

}
