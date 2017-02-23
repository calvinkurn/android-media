package com.tokopedia.seller.topads.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.domain.model.data.Etalase;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopDataSource {

    private final TopAdsEtalaseListMapper topAdsEtalaseListMapper;
    private final TopAdsShopApi topAdsShopApi;

    public TopAdsShopDataSource(TopAdsShopApi topAdsShopApi,
                                TopAdsEtalaseListMapper topAdsEtalaseListMapper) {
        this.topAdsShopApi = topAdsShopApi;
        this.topAdsEtalaseListMapper = topAdsEtalaseListMapper;
    }

    public Observable<List<Etalase>> getEtalaseList(String shopId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        return topAdsShopApi.getShopEtalase(param).map(topAdsEtalaseListMapper);
    }
}
