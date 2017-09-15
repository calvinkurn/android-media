package com.tokopedia.topads.dashboard.data.source.cloud;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.topads.dashboard.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.topads.dashboard.data.source.local.TopAdsEtalaseCacheDataSource;
import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, String> map = new HashMap<>();
        map.put(TopAdsNetworkConstant.PARAM_SHOP_ID, shopId);

        Map <String, String> param = AuthUtil.generateParams(MainApplication.getAppContext(), map);
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
