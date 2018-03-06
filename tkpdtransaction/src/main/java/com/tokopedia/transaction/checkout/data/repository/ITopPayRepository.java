package com.tokopedia.transaction.checkout.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData;

import rx.Observable;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public interface ITopPayRepository {

    Observable<ThanksTopPayData> getThanksTopPay(TKPDMapParam<String, String> param);
}
