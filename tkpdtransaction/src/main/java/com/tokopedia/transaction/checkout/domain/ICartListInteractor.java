package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartListInteractor {

    void getCartList(Subscriber<String> subscriber, TKPDMapParam<String, String> param);
}
