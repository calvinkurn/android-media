package com.tokopedia.seller.goldmerchant.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.featured.data.FeaturedProductDataSource;
import com.tokopedia.seller.goldmerchant.featured.domain.mapper.FeaturedProductMapper;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductRepositoryImpl implements FeaturedProductRepository {

    private FeaturedProductDataSource featuredProductDataSource;
    private ShopInfoRepository shopInfoRepository;
    private FeaturedProductMapper featuredProductMapper;

    public FeaturedProductRepositoryImpl(FeaturedProductDataSource featuredProductDataSource, ShopInfoRepository shopInfoRepository, FeaturedProductMapper featuredProductMapper) {
        this.featuredProductDataSource = featuredProductDataSource;
        this.shopInfoRepository = shopInfoRepository;
        this.featuredProductMapper = featuredProductMapper;
    }

    @Override
    public Observable<FeaturedProductPOSTDomainModel> postFeatureProductData(RequestParams requestParams) {
        return null;
    }

    @Override
    public Observable<FeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams) {
        return featuredProductDataSource.productGETModelObservable(shopInfoRepository.getShopId()).map(featuredProductMapper);
    }
}
