package com.tokopedia.seller.goldmerchant.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductRepositoryImpl implements FeaturedProductRepository {

    public FeaturedProductRepositoryImpl() {
    }

    @Override
    public Observable<FeaturedProductPOSTDomainModel> postFeatureProductData(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<FeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams) {
        return null;
    }
}
