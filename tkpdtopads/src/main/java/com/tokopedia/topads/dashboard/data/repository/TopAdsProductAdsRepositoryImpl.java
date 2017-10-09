package com.tokopedia.topads.dashboard.data.repository;

import com.tokopedia.topads.dashboard.data.factory.TopAdsProductAdFactory;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsProductAdsDataSource;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;

import java.util.List;

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

    @Override
    public Observable<TopAdsDetailProductDomainModel> saveDetailListProduct(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels) {
        TopAdsProductAdsDataSource dataSource = topAdsShopAdFactory.createProductAdsDataSource();
        return dataSource.createDetailProductList(topAdsDetailProductDomainModels);
    }

}
