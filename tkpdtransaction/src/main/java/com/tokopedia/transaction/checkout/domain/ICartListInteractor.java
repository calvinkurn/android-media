package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.view.data.CartListData;
import com.tokopedia.transaction.checkout.view.data.DeleteCartData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartListInteractor {

    void getCartList(Subscriber<CartListData> subscriber, TKPDMapParam<String, String> param);

    void deleteCart(Subscriber<DeleteCartData> subscriber, TKPDMapParam<String, String> param);
}
