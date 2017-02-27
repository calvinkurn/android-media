package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.domain.model.TopAdsDetailShopDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopAdsDataSource {

    private final TopAdsDetailProductMapper topAdsDetailProductMapper;
    private final TopAdsManagementApi topAdsManagementApi;
    private final Context context;

    public TopAdsShopAdsDataSource(Context context, TopAdsManagementApi topAdsManagementApi, TopAdsDetailProductMapper topAdsDetailProductMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsDetailProductMapper = topAdsDetailProductMapper;
    }

    public Observable<TopAdsDetailShopDomainModel> getDetailProduct(String adId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return topAdsManagementApi.getDetailProduct(param).map(topAdsDetailProductMapper);
    }
}
