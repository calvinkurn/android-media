package com.tokopedia.seller.topads.data.factory;

import android.content.Context;

import com.tokopedia.seller.topads.data.mapper.TopAdsDetailGroupDomainMapper;
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
    private TopAdsDetailGroupMapper topAdsDetailGroupMapper;
    private TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper;

    public TopAdsGroupAdFactory(Context context, TopAdsManagementApi topAdsManagementApi,
                                TopAdsSearchGroupMapper topAdsSearchGroupMapper,
                                TopAdsDetailGroupMapper topAdsDetailGroupMapper,
                                TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsSearchGroupMapper;
        this.topAdsDetailGroupMapper = topAdsDetailGroupMapper;
        this.topAdsDetailGroupDomainMapper = topAdsDetailGroupDomainMapper;
    }

    public TopAdsGroupAdsDataSource createGroupAdsDataSource() {
        return new TopAdsGroupAdsDataSource(context, topAdsManagementApi,
                topAdsSearchGroupMapper, topAdsDetailGroupMapper,
                topAdsDetailGroupDomainMapper
                );
    }
}
