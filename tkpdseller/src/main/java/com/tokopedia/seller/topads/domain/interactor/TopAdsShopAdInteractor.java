package com.tokopedia.seller.topads.domain.interactor;

import com.tokopedia.seller.topads.data.model.data.ShopAd;
import com.tokopedia.seller.topads.data.model.request.SearchAdRequest;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsShopAdInteractor {

    void getShopAd(SearchAdRequest searchAdRequest, final ListenerInteractor<ShopAd> listener);

    void unSubscribe();
}
