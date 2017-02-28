package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsProductAdsRepository {
    Observable<TopAdsDetailProductDomainModel> getDetail(String adId);

    Observable<TopAdsDetailProductDomainModel> saveDetail(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel);
}
