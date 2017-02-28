package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.factory.TopAdsProductAdFactory;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsProductAdsDataSource;
import com.tokopedia.seller.topads.domain.TopAdsProductAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;

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
}
