package com.tokopedia.seller.topads.interactor;

import com.tokopedia.seller.topads.model.data.ShopAd;
import com.tokopedia.seller.topads.model.request.SearchAdRequest;
import com.tokopedia.seller.topads.model.response.DataResponse;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public interface TopAdsShopAdInteractor {

    void getShopAd(SearchAdRequest searchAdRequest, final ListenerInteractor<ShopAd> listener);

    void unSubscribe();
}
