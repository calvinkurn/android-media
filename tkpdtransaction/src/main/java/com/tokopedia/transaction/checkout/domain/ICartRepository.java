package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartRepository {

    Observable<String> getCartList(TKPDMapParam<String, String> param);

}
