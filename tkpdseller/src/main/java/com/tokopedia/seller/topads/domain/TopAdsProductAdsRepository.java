package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.seller.topads.domain.model.data.ProductAdBulkAction;
import com.tokopedia.seller.topads.domain.model.response.DataResponse;

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
