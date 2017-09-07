package com.tokopedia.seller.goldmerchant.featured.data;

import com.tokopedia.seller.common.data.mapper.SimpleResponseMapper;
import com.tokopedia.seller.goldmerchant.featured.data.cloud.api.FeaturedProductApi;
import com.tokopedia.seller.goldmerchant.featured.data.model.FeaturedProductGETModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/7/17.
 */

public class FeaturedProductDataSource {
    private FeaturedProductApi featuredProductApi;

    @Inject
    public FeaturedProductDataSource(FeaturedProductApi featuredProductApi) {
        this.featuredProductApi = featuredProductApi;
    }

    public Observable<FeaturedProductGETModel> productGETModelObservable(String shopId) {
        return featuredProductApi.getFeaturedProduct(shopId).map(new SimpleResponseMapper<FeaturedProductGETModel>());
    }
}
