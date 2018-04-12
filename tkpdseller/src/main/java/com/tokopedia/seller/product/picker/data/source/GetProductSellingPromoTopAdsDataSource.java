package com.tokopedia.seller.product.picker.data.source;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by hadi-puta on 11/04/18.
 */

public class GetProductSellingPromoTopAdsDataSource {
    private final GetProductSellingPromoTopAdsDataSourceCloud dataSourceCloud;

    public GetProductSellingPromoTopAdsDataSource(GetProductSellingPromoTopAdsDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<String> checkPromoAds(RequestParams requestParams) {
        return dataSourceCloud.checkPromoAds(requestParams);
    }
}
