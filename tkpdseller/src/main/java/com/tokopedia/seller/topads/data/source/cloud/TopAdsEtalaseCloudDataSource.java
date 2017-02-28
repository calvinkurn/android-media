package com.tokopedia.seller.topads.data.source.cloud;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;
import com.tokopedia.seller.topads.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.seller.topads.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.data.source.local.TopAdsEtalaseCacheDataSource;
import com.tokopedia.seller.topads.domain.model.data.Etalase;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by hendry on 2/20/17.
 */
public class TopAdsEtalaseCloudDataSource implements TopAdsEtalaseDataSource {

    private final TopAdsEtalaseListMapper topAdsEtalaseListMapper;
    private final TopAdsShopApi topAdsShopApi;

    public TopAdsEtalaseCloudDataSource(TopAdsShopApi topAdsShopApi,
                                        TopAdsEtalaseListMapper topAdsEtalaseListMapper) {
        this.topAdsShopApi = topAdsShopApi;
        this.topAdsEtalaseListMapper = topAdsEtalaseListMapper;
    }

    public Observable<List<Etalase>> getEtalaseList(String shopId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);
        return topAdsShopApi.getShopEtalase(param)
                .map(topAdsEtalaseListMapper)
                .doOnNext(new Action1<List<Etalase>>() {
                    @Override
                    public void call(List<Etalase> etalaseList) {
                        TopAdsEtalaseCacheDataSource.saveEtalaseListToCache(etalaseList);
                    }
                });
    }
}
