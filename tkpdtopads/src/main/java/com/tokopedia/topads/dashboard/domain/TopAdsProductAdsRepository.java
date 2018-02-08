package com.tokopedia.topads.dashboard.domain;

import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsProductAdsRepository {
    Observable<TopAdsDetailProductDomainModel> getDetail(String adId);

    Observable<TopAdsDetailProductDomainModel> saveDetail(TopAdsDetailProductDomainModel topAdsDetailProductDomainModel);

    Observable<ProductAdBulkAction> moveProductGroup(String adId, String groupId, String shopId);


    Observable<TopAdsDetailProductDomainModel> saveDetailListProduct(List<TopAdsDetailProductDomainModel> topAdsDetailProductDomainModels);
}
