package com.tokopedia.loyalty.view.interactor;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public interface IPromoInteractor {

    void getPromoMenuList(TKPDMapParam<String, String> param, Subscriber<List<PromoMenuData>> subscriber);

    void getPromoList(TKPDMapParam<String, String> param, Subscriber<List<PromoData>> subscriber);

}
