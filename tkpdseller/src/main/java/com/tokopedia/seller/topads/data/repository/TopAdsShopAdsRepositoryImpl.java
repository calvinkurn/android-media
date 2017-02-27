package com.tokopedia.seller.topads.data.repository;

import com.tokopedia.seller.topads.data.factory.TopAdsShopAdFactory;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsShopAdsDataSource;
import com.tokopedia.seller.topads.domain.TopAdsShopAdsRepository;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */

public class TopAdsShopAdsRepositoryImpl implements TopAdsShopAdsRepository {

    private final TopAdsShopAdFactory topAdsShopAdFactory;

    public TopAdsShopAdsRepositoryImpl(TopAdsShopAdFactory topAdsShopAdFactory) {
        this.topAdsShopAdFactory = topAdsShopAdFactory;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> getDetail(String adId) {
        TopAdsShopAdsDataSource topAdsShopAdsDataSource = topAdsShopAdFactory.createShopAdsDataSource();
        return topAdsShopAdsDataSource.getDetailProduct(adId);
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> saveDetail(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel) {
        TopAdsShopAdsDataSource topAdsShopAdsDataSource = topAdsShopAdFactory.createShopAdsDataSource();
        return topAdsShopAdsDataSource.saveDetailProduct(topAdsDetailShopDomainModel);
    }
}
