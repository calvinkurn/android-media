package com.tokopedia.seller.goldmerchant.featured.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.goldmerchant.featured.data.FeaturedProductDataSource;
import com.tokopedia.seller.goldmerchant.featured.data.model.PostFeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.domain.interactor.FeaturedProductPOSTUseCase;
import com.tokopedia.seller.goldmerchant.featured.domain.mapper.FeaturedProductMapper;
import com.tokopedia.seller.goldmerchant.featured.domain.mapper.FeaturedProductPOSTMapper;
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
    private FeaturedProductPOSTMapper featuredProductPOSTMapper;

    public FeaturedProductRepositoryImpl(FeaturedProductDataSource featuredProductDataSource,
                                         ShopInfoRepository shopInfoRepository,
                                         FeaturedProductMapper featuredProductMapper,
                                         FeaturedProductPOSTMapper featuredProductPOSTMapper) {
        this.featuredProductDataSource = featuredProductDataSource;
        this.shopInfoRepository = shopInfoRepository;
        this.featuredProductMapper = featuredProductMapper;
        this.featuredProductPOSTMapper = featuredProductPOSTMapper;
    }

    @Override
    public Observable<FeaturedProductPOSTDomainModel> postFeatureProductData(RequestParams requestParams) {
        PostFeaturedProductModel postFeaturedProductModel
                = (PostFeaturedProductModel) requestParams.getObject(FeaturedProductPOSTUseCase.FEATURED_PRODUCT_MODEL_PARAM);

        postFeaturedProductModel.setShopId(shopInfoRepository.getShopId());


        return featuredProductDataSource.productPOSTModelObservable(postFeaturedProductModel).map(featuredProductPOSTMapper);
    }

    @Override
    public Observable<FeaturedProductDomainModel> getFeatureProductData(RequestParams requestParams) {
        return featuredProductDataSource.productGETModelObservable(shopInfoRepository.getShopId()).map(featuredProductMapper);
    }
}
