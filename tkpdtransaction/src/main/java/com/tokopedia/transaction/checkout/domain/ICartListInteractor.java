package com.tokopedia.transaction.checkout.domain;

import com.google.gson.JsonObject;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartListInteractor {

    void getCartList(Subscriber<List<CartItemData>> subscriber, TKPDMapParam<String, String> param);

    void deleteCart(Subscriber<String> subscriber, JsonObject param);
}
