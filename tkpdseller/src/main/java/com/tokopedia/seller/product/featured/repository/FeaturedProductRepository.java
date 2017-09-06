package com.tokopedia.seller.product.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.seller.product.featured.domain.model.FeaturedProductPOSTDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public interface FeaturedProductRepository {
    Observable<FeaturedProductPOSTDomainModel> postFeatureProductData(RequestParams requestParams);

    Observable<FeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams);
}
