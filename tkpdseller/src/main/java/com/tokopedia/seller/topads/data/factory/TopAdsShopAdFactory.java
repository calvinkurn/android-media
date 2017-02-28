package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsShopAdsDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsDetailProductMapper topAdsSearchGroupMapper;

    public TopAdsShopAdFactory(Context context, TopAdsManagementApi topAdsManagementApi,
                               TopAdsDetailProductMapper topAdsSearchGroupMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
    }

    public TopAdsShopAdsDataSource createShopAdsDataSource() {
        return new TopAdsShopAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper);
    }
}
