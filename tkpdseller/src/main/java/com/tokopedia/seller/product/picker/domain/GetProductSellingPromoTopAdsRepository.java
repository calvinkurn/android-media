package com.tokopedia.seller.product.picker.domain;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by hadi-putra on 11/04/18.
 */

public interface GetProductSellingPromoTopAdsRepository {
    Observable<String> getProductPromoTopAds(RequestParams requestParams);
}
