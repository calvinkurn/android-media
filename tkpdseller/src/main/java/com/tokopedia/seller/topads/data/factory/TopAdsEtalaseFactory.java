package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsEtalaseListMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsGroupAdsDataSource;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsShopDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsShopApi;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsEtalaseFactory {

    private final TopAdsShopApi topAdsShopApi;
    private final TopAdsEtalaseListMapper topAdsEtalaseListMapper;

    public TopAdsEtalaseFactory( TopAdsShopApi topAdsShopApi,
                                TopAdsEtalaseListMapper topAdsEtalaseListMapper) {
        this.topAdsShopApi = topAdsShopApi;
        this.topAdsEtalaseListMapper = topAdsEtalaseListMapper;
    }

    public TopAdsShopDataSource createEtalaseDataSource() {
        return new TopAdsShopDataSource(topAdsShopApi, topAdsEtalaseListMapper);
    }
}
