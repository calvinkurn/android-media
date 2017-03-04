package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupMapper;
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
    private TopAdsDetailGroupMapper mTopAdsDetailGroupMapper;

    public TopAdsGroupAdFactory(Context context, TopAdsManagementApi topAdsManagementApi,
                                TopAdsSearchGroupMapper topAdsSearchGroupMapper,
                                TopAdsDetailGroupMapper topAdsDetailGroupMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
        this.mTopAdsDetailGroupMapper = topAdsDetailGroupMapper;
    }

    public TopAdsGroupAdsDataSource createGroupAdsDataSource() {
        return new TopAdsGroupAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper, mTopAdsDetailGroupMapper);
    }
}
