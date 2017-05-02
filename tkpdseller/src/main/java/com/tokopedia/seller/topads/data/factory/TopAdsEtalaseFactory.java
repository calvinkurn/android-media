package com.tokopedia.seller.topads.data.factory;

import com.tokopedia.seller.topads.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.seller.topads.data.source.TopAdsEtalaseDataSource;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsEtalaseCloudDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;
import com.tokopedia.seller.topads.data.source.local.TopAdsEtalaseCacheDataSource;

/**
 * Created by Hendry on 2/24/17.
 */
public class TopAdsEtalaseFactory {

    private final TopAdsShopApi topAdsShopApi;
    private final TopAdsEtalaseListMapper topAdsEtalaseListMapper;
    private final TopAdsEtalaseCacheDataSource topAdsShopCacheDataSource;

    public TopAdsEtalaseFactory(TopAdsShopApi topAdsShopApi,
                                TopAdsEtalaseListMapper topAdsEtalaseListMapper,
                                TopAdsEtalaseCacheDataSource topAdsShopCacheDataSource) {
        this.topAdsShopApi = topAdsShopApi;
        this.topAdsEtalaseListMapper = topAdsEtalaseListMapper;
        this.topAdsShopCacheDataSource= topAdsShopCacheDataSource;
    }

    public TopAdsEtalaseDataSource createEtalaseDataSource() {
        if (TopAdsEtalaseCacheDataSource.isCacheExpired()) {
            return new TopAdsEtalaseCloudDataSource(topAdsShopApi,
                    topAdsEtalaseListMapper);
        }
        return topAdsShopCacheDataSource;
    }
}
