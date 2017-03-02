package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsCreateGroupMapper;
import com.tokopedia.seller.topads.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.seller.topads.data.source.cloud.TopAdsGroupAdsDataSource;
import com.tokopedia.seller.topads.data.source.cloud.apiservice.api.TopAdsManagementApi;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGroupAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsSearchGroupMapper topAdsSearchGroupMapper;
    private TopAdsCreateGroupMapper topAdsCreateGroupMapper;

    public TopAdsGroupAdFactory(Context context, TopAdsManagementApi topAdsManagementApi,
                                TopAdsSearchGroupMapper topAdsSearchGroupMapper,
                                TopAdsCreateGroupMapper topAdsCreateGroupMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
        this.topAdsCreateGroupMapper = topAdsCreateGroupMapper;
    }

    public TopAdsGroupAdsDataSource createGroupAdsDataSource() {
        return new TopAdsGroupAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper, topAdsCreateGroupMapper);
    }
}
