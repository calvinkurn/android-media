package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsShopAdsRepository {
    Observable<TopAdsDetailShopDomainModel> getDetail(String adId);

    Observable<TopAdsDetailShopDomainModel> saveDetail(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel);

    Observable<TopAdsDetailShopDomainModel> createDetailShop(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel);
}
