package com.tokopedia.topads.dashboard.data.factory;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupDomainMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailGroupMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsSearchGroupMapper;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsGroupAdsDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGroupAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsSearchGroupMapper topAdsSearchGroupMapper;
    private TopAdsDetailGroupMapper topAdsDetailGroupMapper;
    private TopAdsDetailGroupDomainMapper topAdsDetailGroupDomainMapper;

    @Inject
    public TopAdsGroupAdFactory(@ApplicationContext Context context, TopAdsManagementApi topAdsManagementApi,
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
