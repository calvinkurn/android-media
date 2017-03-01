package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsProductAdsDataSource;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsProductAdsRepositoryImpl implements TopAdsProductAdsRepository {

    private final TopAdsProductAdFactory topAdsShopAdFactory;

    public TopAdsProductAdsRepositoryImpl(TopAdsProductAdFactory topAdsShopAdFactory) {
        this.topAdsShopAdFactory = topAdsShopAdFactory;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> getDetail(String adId) {
        TopAdsProductAdsDataSource dataSource = topAdsShopAdFactory.createProductAdsDataSource();
        return dataSource.getDetailProduct(adId);
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> saveDetail(TopAdsDetailProductDomainModel domainModel) {
        TopAdsProductAdsDataSource dataSource = topAdsShopAdFactory.createProductAdsDataSource();
        return dataSource.saveDetailProduct(domainModel);
    }

    @Override
    public Observable<ProductAdBulkAction> moveProductGroup(String adId, String groupId, String shopId) {
        TopAdsProductAdsDataSource topAdsProductAdsDataSource = topAdsShopAdFactory.createProductAdsDataSource();
        return topAdsProductAdsDataSource.moveProductGroup(adId, groupId, shopId);
    }
}
