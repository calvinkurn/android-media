package com.tokopedia.seller.product.picker.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.picker.data.source.GetProductSellingPromoTopAdsDataSource;
import com.tokopedia.seller.product.picker.domain.GetProductSellingPromoTopAdsRepository;

import java.util.Map;

import rx.Observable;

/**
 * Created by hadi-putra on 11/04/18.
 */

public class GetProductSellingPromoTopAdsRepositoryImpl implements GetProductSellingPromoTopAdsRepository {

    private final GetProductSellingPromoTopAdsDataSource dataSource;

    public GetProductSellingPromoTopAdsRepositoryImpl(GetProductSellingPromoTopAdsDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Observable<String> getProductPromoTopAds(RequestParams requestParams) {
        return dataSource.checkPromoAds(requestParams);
    }
}
